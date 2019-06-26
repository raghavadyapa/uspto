/*package com.ipcybris.patent;

import com.google.auth.oauth2.GoogleCredentials;
import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.Compression;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.slf4j.impl.StaticLoggerBinder;

import static org.apache.beam.sdk.io.Compression.GZIP;
import static org.apache.beam.sdk.io.Compression.ZIP;
import static org.apache.beam.sdk.io.TextIO.*;
import static org.apache.beam.sdk.values.TypeDescriptors.strings;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class BeamSample {

    public static void main(String[] args) throws IOException {

        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline pipeline = Pipeline.create(options);
           // GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("C:\\Users\\techolution\\Downloads\\GoogleCloudStorageAccessKey\\filestoreFullAccessKey\\filestore-0b551ac6c820.json"));


        PCollection<String> lines = pipeline.apply("read from file", TextIO.read().from("gs://patenttest/sample.txt"));
        lines.apply(TextIO.write().to("gs://patenttest/text.txt").withoutSharding());
       pipeline.run().waitUntilFinish();



        /*DataflowPipelineOptions options = PipelineOptionsFactory.as(DataflowPipelineOptions.class);
        options.setProject("filestore");
        options.setStagingLocation("gs://GoogleStorageBucket/staging");
        options.setRunner(DataflowRunner.class);
        DataflowRunner.fromOptions(options);

        Pipeline p = Pipeline.create(options);*/







       /* //"C:\\Users\\techolution\\Documents\\sample.zip"));
            // withCompression can be omitted - by default compression is detected from the filename.
            .apply(FileIO.readMatches().withCompression(GZIP))
            .apply(MapElements
                    // uses imports from TypeDescriptors
                    .into(KVs(strings(), strings()))
                    .via((FileIO.ReadableFile f) -> KV.of(
                            f.getMetadata().resourceId().toString(), f.readFullyAsUTF8String())));*/

