/*package com.ipcybris.patent;

import com.google.api.gax.paging.Page;
import com.google.api.services.storage.model.Bucket;
import com.google.cloud.storage.Blob;

class listing {
    private final Bucket bucket;

    public Page<Blob> listBlobs() {
        // [START listBlobs]
        Page<Blob> blobs = bucket.list();
        for (Blob blob : blobs.iterateAll()) {
            // do something with the blob
        }
        // [END listBlobs]
        return blobs;
    }
    public static  void main(String[] args)
    {

    }
}
*/