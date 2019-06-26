/*package com.ipcybris.patent;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/** A snippet for Google Cloud Storage showing how to create a blob.
public class UploadAPI {

    public static void main(String... args) {
        // [START storage_upload_file]
        Storage storage = StorageOptions.get.getService();
        BlobId blobId = BlobId.of("bucket", "blob_name");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
        Blob blob = storage.create(blobInfo, "Hello, Cloud Storage!".getBytes(UTF_8));
        // [END storage_upload_file]
    }
}
/*import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.cloud.storage.*;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UploadAPI {

    /*public static HttpResponse executeGet(
            HttpTransport transport, JsonFactory jsonFactory, String accessToken, GenericUrl url)
            throws IOException {
        Credential credential =
                new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(accessToken);
        HttpRequestFactory requestFactory = transport.createRequestFactory(credential);
        return requestFactory.buildGetRequest(url).execute();
    }*/
/*
    public static void main(String [] args) throws Exception {
        // Instantiates a client
       // Storage storage = StorageOptions.getDefaultInstance().getService();
        // The name for the new bucket
        //String bucketName = "raghava1";  // "my-new-bucket";
        // Creates the new bucket
        //Bucket bucket = storage.create(BucketInfo.of(bucketName));
        //System.out.printf("Bucket %s created.%n", bucket.getName());
   Storage storage = StorageOptions.newBuilder().setProjectId("firestore").build().getService();
    BlobId blobId = BlobId.of("patenttest", "USD0452761-20020108-D00000");
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/tiff").build();
    Blob blob = storage.create(blobInfo, "Hello, Cloud Storage!".getBytes(UTF_8));

}

}*/
