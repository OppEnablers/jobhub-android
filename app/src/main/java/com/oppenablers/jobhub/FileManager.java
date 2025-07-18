package com.oppenablers.jobhub;

import android.content.Context;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.File;

public class FileManager {
    private static final String IDENTITY_POOL_ID = BuildConfig.COGNITO_POOL_ID;
    private static final Regions REGION = Regions.fromName(BuildConfig.AWS_REGION);
    private static final String BUCKET_NAME = BuildConfig.BUCKET_NAME;

    private final TransferUtility transferUtility;

    public FileManager(Context context) {

        CognitoCachingCredentialsProvider creds =
                new CognitoCachingCredentialsProvider(
                        context.getApplicationContext(),
                        IDENTITY_POOL_ID,
                        REGION);

        AmazonS3Client s3 = new AmazonS3Client(creds, Region.getRegion(REGION));

        transferUtility = TransferUtility.builder()
                .context(context.getApplicationContext())
                .s3Client(s3)
                .defaultBucket(BUCKET_NAME)
                .build();
    }

    public TransferObserver upload(String key, File file, TransferListener listener) {
        if (file.length() > 5 * 1024 * 1024) { // 5MB in bytes
            Toast.makeText(this, "File size exceeds 5MB limit.", Toast.LENGTH_SHORT).show();
            return;
        }

        ObjectMetadata metadata = new ObjectMetadata();
        TransferObserver ob = transferUtility.upload(
                BUCKET_NAME,
                key,
                file,
                metadata,
                CannedAccessControlList.PublicRead
        );
        if (listener != null) ob.setTransferListener(listener);
        return ob;
    }

    public TransferObserver download(String key, File dest, TransferListener listener) {
        TransferObserver ob = transferUtility.download(BUCKET_NAME, key, dest);
        if (listener != null) ob.setTransferListener(listener);
        return ob;
    }

    public static abstract class SimpleListener implements TransferListener {
        @Override public void onStateChanged(int id, TransferState state) { }
        @Override public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            if (bytesTotal > 0) {
                int pct = (int)((bytesCurrent * 100) / bytesTotal);
                android.util.Log.d("S3", "Transfer " + id + " = " + pct + "%");
            }
        }
        @Override public void onError(int id, Exception ex) {
            android.util.Log.e("S3", "Error on transfer " + id, ex);
        }
    }
}
