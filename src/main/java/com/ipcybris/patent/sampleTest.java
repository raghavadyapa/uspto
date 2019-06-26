//package com.ipcybris.patent;
//
//import com.google.api.client.util.Lists;
//import com.google.api.gax.paging.Page;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Bucket;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Collections;
//
//public class sampleTest {
//    public static void main(String []args) {
//
//        public void authImplicit()
//    {
//            // If you don't specify credentials when constructing the client, the client library will
//            // look for credentials via the environment variable GOOGLE_APPLICATION_CREDENTIALS.
//
//            //Storage storage = StorageOptions.getDefaultInstance().getService();
//            Storage storage=StorageOptions.getDefaultInstance().getService();
//
//            System.out.println("Buckets:");
//            Page<com.google.cloud.storage.Bucket> buckets = storage.list();
//            for (com.google.cloud.storage.Bucket bucket : buckets.iterateAll()) {
//                System.out.println(bucket.toString());
//            }
//        }
//
//         void authExplicit(String String jsonPath;
//        jsonPath) throws IOException {
//            // You can specify a credential file by providing a path to GoogleCredentials.
//            // Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
//            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
//                    .createScoped(Lists.newArrayList(Collections.singleton("https://www.googleapis.com/auth/cloud-platform")));
//            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//
//            System.out.println("Buckets:");
//            Page<Bucket> buckets = storage.list();
//            // for (Bucket bucket : buckets.iterateAll()) {
//            //   System.out.println(bucket.toString());
//            //  }
//        }
//    }
//}
