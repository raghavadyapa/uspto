/*package com.ipcybris.patent;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.StorageOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.util.GcsUtil;
import org.apache.beam.sdk.util.gcsfs.GcsPath;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SimpleUnzip {

   // private static final Logger LOG = LoggerFactory.getLogger(SimpleUnzip.class);

    public static void main(String[] args) throws IOException {

       /* StorageOptions.Builder optionsBuilder = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials
                        .fromStream(new FileInputStream("C:\\Users\\techolution\\Downloads\\Google cloud storage access key\\filestore-16b513a0d4c0.json")));
        Pipeline p = Pipeline.create(
                PipelineOptionsFactory.fromArgs(args).withValidation().create());

        GcsUtil.GcsUtilFactory factory = new GcsUtil.GcsUtilFactory();
        GcsUtil util = factory.create(p.getOptions());
        try {
            List<GcsPath> gcsPaths = util.expand(GcsPath.fromUri("gs://zip_files1/*.zip"));
            List<String> paths = new ArrayList<String>();

            for (GcsPath gcsp : gcsPaths) {
                paths.add(gcsp.toString());
            }
            p.apply(Create.of(paths))
                    .apply(ParDo.of(new UnzipFN()));
            p.run();

        } catch (Exception e) {
//            LOG.error(e.getMessage());
        }


    }

    public static class UnzipFN extends DoFn<String, Long> {
        private static final long serialVersionUID = 2015166770614756341L;
        private long filesUnzipped = 0;

      //  @Override
        public void processElement(ProcessContext c) {
            String p = c.element();
            GcsUtil.GcsUtilFactory factory = new GcsUtil.GcsUtilFactory();
            GcsUtil u = factory.create(c.getPipelineOptions());
            byte[] buffer = new byte[100000000];
            try {
                SeekableByteChannel sek = u.open(GcsPath.fromUri(p));
                InputStream is = Channels.newInputStream(sek);
                BufferedInputStream bis = new BufferedInputStream(is);
                ZipInputStream zis = new ZipInputStream(bis);
                ZipEntry ze = zis.getNextEntry();
                while (ze != null) {
//                    LOG.info("Unzipping File {}", ze.getName());
                    WritableByteChannel wri = u.create(GcsPath.fromUri("gs://zip_files1" + ze.getName()), getType(ze.getName()));
                    OutputStream os = Channels.newOutputStream(wri);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        os.write(buffer, 0, len);
                    }
                    os.close();
                    filesUnzipped++;
                    ze = zis.getNextEntry();

                }
                zis.closeEntry();
                zis.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            c.output(filesUnzipped);
        }

        private String getType(String fName) {
            if (fName.endsWith(".zip")) {
                return "application/x-zip-compressed";
            } else {
                return "text/plain";
            }
        }
    }
}
*/
