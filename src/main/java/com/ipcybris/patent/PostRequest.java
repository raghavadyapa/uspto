/*package com.ipcybris.patent;

import com.google.cloud.storage.*;

import java.io.*;
import java.nio.file.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.*;


import javax.net.ssl.HttpsURLConnection;

import static com.google.common.net.HttpHeaders.USER_AGENT;
import static java.nio.charset.StandardCharsets.UTF_8;

public class PostRequest {
    public static void main(String []args) throws Exception {

        String url = "https://www.googleapis.com/upload/storage/v1/b/patenttest/o?";//uploadType=media&name=myObject";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type","image/tiff");
        con.setRequestProperty("Authorization","Bearer ya29.GlsZB0SBYvEUffuxA_GolKx_1JFqi2XVJbZs0yBM8s97nsfHBIjM0ElBBu1XfnS4gHQBh9XuSVuIYgqHpBgtB7fsDdhZ3LNoyRVEks0ixRubDX1oa-Ei64pMXwdN");


     //   con.setRequestProperty("User-Agent", USER_AGENT);
     //   con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "uploadType=media&name=myObject1";

        // Send post request
        con.setDoOutput(true);
        File f=new File("C:\\Users\\techolution\\Desktop\\USD0452767-20020108-D00000.tif");
        byte [] b=new byte[(int)f.length()];
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());//(new FileOutputStream("C:\\Users\\techolution\\Desktop\\USD0452767-20020108-D00000.tif"));//con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.write(b);
        wr.flush();
        wr.close();
       // Storage storage = StorageOptions.getDefaultInstance().getService();
        // The name for the new bucket
       // String bucketName = "raghava1";  // "my-new-bucket";
        // Creates the new bucket
       // Bucket bucket = storage.create(BucketInfo.of(bucketName));
        //System.out.printf("Bucket %s created.%n", bucket.getName());
      /*  Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of("bucket", "blob_name");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("images/tiff").build();
        Blob blob = storage.create(blobInfo, "Hello, Cloud Storage!".getBytes(UTF_8));


    }

}
*/