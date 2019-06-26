package com.ipcybris.patent;

/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


import com.google.common.annotations.VisibleForTesting;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.beam.repackaged.beam_sdks_java_core.org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.beam.repackaged.beam_sdks_java_core.org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.Compression;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.fs.MatchResult;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.util.GcsUtil;
import org.apache.beam.sdk.util.gcsfs.GcsPath;
import org.apache.beam.sdk.values.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FilenameUtils;

/**
 * This pipeline decompresses file(s) from Google Cloud Storage and re-uploads them to a destination
 * location.
 *
 * <p><b>Parameters</b>
 *
 * <p>The {@code --inputFilePattern} parameter specifies a file glob to process. Files found can be
 * expressed in the following formats:
 *
 * <pre>
 * --inputFilePattern=gs://bucket-name/compressed-dir/*
 * --inputFilePattern=gs://bucket-name/compressed-dir/demo*.gz
 * </pre>
 *
 * <p>The {@code --outputDirectory} parameter can be expressed in the following formats:
 *
 * <pre>
 * --outputDirectory=gs://bucket-name
 * --outputDirectory=gs://bucket-name/decompressed-dir
 * </pre>
 *
 * <p>The {@code --outputFailureFile} parameter indicates the file to write the names of the files
 * which failed decompression and their associated error messages. This file can then be used for
 * subsequent processing by another process outside of Dataflow (e.g. send an email with the
 * failures, etc.). If there are no failures, the file will still be created but will be empty. The
 * failure file structure contains both the file that caused the error and the error message in CSV
 * format. The file will contain one header row and two columns (Filename, Error). The filename
 * output to the failureFile will be the full path of the file for ease of debugging.
 *
 * <pre>
 * --outputFailureFile=gs://bucket-name/decompressed-dir/failed.csv
 * </pre>
 *
 * <p>Example Output File:
 *
 * <pre>
 * Filename,Error
 * gs://docs-demo/compressedFile.gz, File is malformed or not compressed in BZIP2 format.
 * </pre>
 *
 * <p><b>Example Usage</b>
 *
 * <pre>
 * mvn compile exec:java \
 * -Dexec.mainClass=com.google.cloud.teleport.templates.BulkDecompressor \
 * -Dexec.cleanupDaemonThreads=false \
 * -Dexec.args=" \
 * --project=${PROJECT_ID} \
 * --stagingLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_FOLDER}/staging \
 * --tempLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_FOLDER}/temp \
 * --runner=DataflowRunner \
 * --inputFilePattern=gs://${PROJECT_ID}/compressed-dir/*.gz \
 * --outputDirectory=gs://${PROJECT_ID}/decompressed-dir \
 * --outputFailureFile=gs://${PROJECT_ID}/decompressed-dir/failed.csv"
 * </pre>
 */
public class UnzipParent {

    /** The logger to output status messages to. */
    private static final Logger LOG = LoggerFactory.getLogger(UnzipParent.class);

    /**
     * A list of the {@link Compression} values excluding {@link Compression#AUTO} and {@link
     * Compression#UNCOMPRESSED}.
     */
    @VisibleForTesting
    static final Set<Compression> SUPPORTED_COMPRESSIONS =
            Stream.of(Compression.values())
                    .filter(value -> value != Compression.AUTO && value != Compression.UNCOMPRESSED)
                    .collect(Collectors.toSet());

    /** The error msg given when the pipeline matches a file but cannot determine the compression. */
    @VisibleForTesting
    static final String UNCOMPRESSED_ERROR_MSG =
            "Skipping file %s because it did not match any compression mode (%s)";

    @VisibleForTesting
    static final String MALFORMED_ERROR_MSG =
            "The file resource %s is malformed or not in %s compressed format.";

    /** The tag used to identify the main output of the {@link Decompress} DoFn. */
    @VisibleForTesting
    static final TupleTag<String> DECOMPRESS_MAIN_OUT_TAG = new TupleTag<String>() {};

    /** The tag used to identify the dead-letter sideOutput of the {@link Decompress} DoFn. */
    @VisibleForTesting
    static final TupleTag<KV<String, String>> DEADLETTER_TAG = new TupleTag<KV<String, String>>() {};

    /**
     * The {@link Options} class provides the custom execution options passed by the executor at the
     * command-line.
     */
    public interface Options extends PipelineOptions {
        @Description("The input file pattern to read from (e.g. gs://bucket-name/compressed/*.gz)")
        @Required
        ValueProvider<String> getInputFilePattern();

        void setInputFilePattern(ValueProvider<String> value);

        @Description("The output location to write to (e.g. gs://bucket-name/decompressed)")
        @Required
        ValueProvider<String> getOutputDirectory();

        void setOutputDirectory(ValueProvider<String> value);

        ValueProvider<String> getOutputTopic();

        void setOutputTopic(ValueProvider<String> value);
    }

    /**
     * The main entry-point for pipeline execution. This method will start the pipeline but will not
     * wait for it's execution to finish. If blocking execution is required, use the {@link
     * BulkDecompressor#run(Options)} method to start the pipeline and invoke {@code
     * result.waitUntilFinish()} on the {@link PipelineResult}.
     *
     * @param args The command-line args passed by the executor.
     */
    public static void main(String[] args) {

        Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);

        run(options);
    }

    /**
     * Runs the pipeline to completion with the specified options. This method does not wait until the
     * pipeline is finished before returning. Invoke {@code result.waitUntilFinish()} on the result
     * object to block until the pipeline is finished running if blocking programmatic execution is
     * required.
     *
     * @param options The execution options.
     * @return The pipeline result.
     */
    public static PipelineResult run(Options options) {

        /*
         * Steps:
         *   1) Decompress the input files found and output them to the output directory
         */

        // Create the pipeline
        Pipeline pipeline = Pipeline.create(options);

        // Run the pipeline over the work items.
        PCollection<Long> decompressOut =
                pipeline
                        .apply("MatchFile(s)", FileIO.match().filepattern(options.getInputFilePattern()))
                        .apply(
                                "DecompressFile(s)",
                                ParDo.of(new DecompressNew(options.getInputFilePattern(), options.getOutputDirectory())));

        return pipeline.run();
    }

    /**
     * Performs the decompression of an object on Google Cloud Storage and uploads the decompressed
     * object back to a specified destination location.
     */
    @SuppressWarnings("serial")
    public static class DecompressNew extends DoFn<MatchResult.Metadata,Long>{
        private static final long serialVersionUID = 2015166770614756341L;
        private long filesUnzipped=0;

        private final ValueProvider<String> inputLocation;
        private final ValueProvider<String> destinationLocation;

        DecompressNew(ValueProvider<String> inputLocation, ValueProvider<String> destinationLocation) {
            this.inputLocation = inputLocation;
            this.destinationLocation = destinationLocation;
        }

        @ProcessElement
        public void processElement(ProcessContext c){
            String p = c.element().resourceId().getScheme();
            GcsUtil.GcsUtilFactory factory = new GcsUtil.GcsUtilFactory();
            GcsUtil u = factory.create(c.getPipelineOptions());
            byte[] buffer = new byte[100000000];
            try{
                SeekableByteChannel sek = u.open(GcsPath.fromUri(this.inputLocation.get()));

/*
  Check for zip or tar file types
 */
                String ext = FilenameUtils.getExtension(this.inputLocation.get());
                if (ext.equalsIgnoreCase("zip") ) {
                    InputStream is;
                    is = Channels.newInputStream(sek);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ZipInputStream zis = new ZipInputStream(bis);
                    ZipEntry ze = zis.getNextEntry();
                    while(ze!=null){
                        if (getType(ze.getName()) == ".pdf") {

                            LoggerFactory.getLogger("unzip").info("Unzipping File {}", ze.getName());
                            WritableByteChannel wri = u.create(GcsPath.fromUri("gs://uspto_data/pdf/" + ze.getName()), getType(ze.getName()));
                            OutputStream os = Channels.newOutputStream(wri);
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                os.write(buffer, 0, len);
                            }
                            os.close();
                            filesUnzipped++;
                        }
                        ze=zis.getNextEntry();
                    }
                    zis.closeEntry();
                    zis.close();
                } else if(ext.equalsIgnoreCase("tar")) {
                    InputStream is;
                    is = Channels.newInputStream(sek);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    TarArchiveInputStream tis = new TarArchiveInputStream(bis);
                    TarArchiveEntry te = tis.getNextTarEntry();
                    while(te!=null){
                        if (getType(te.getName()) == ".pdf") {

                            LoggerFactory.getLogger("unzip").info("Unzipping File {}", te.getName());
                            WritableByteChannel wri = u.create(GcsPath.fromUri("gs://uspto_data/pdf/" + "untar" + te.getName()), getType(te.getName()));
                            OutputStream os = Channels.newOutputStream(wri);
                            int len;
                            while ((len = tis.read(buffer)) > 0) {
                                os.write(buffer, 0, len);
                            }
                            os.close();
                            filesUnzipped++;
                        }
                        te=tis.getNextTarEntry();
                    }
                    tis.close();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            c.output(filesUnzipped);
        }

        private String getType(String fName){
            if(fName.endsWith(".zip")){
                return "application/x-zip-compressed";
            } else if(fName.endsWith(".tar")){
                return "application/x-tar";
            }
            else if(fName.endsWith(".tar")){
                return "application/pdf";
            }
            else {
                return "text/plain";
            }
        }
    }
}
