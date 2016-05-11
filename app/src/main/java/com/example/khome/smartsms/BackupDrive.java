package com.example.khome.smartsms;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BackupDrive extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "upload_file";
    private static final int REQUEST_CODE = 101;
    private File textFile;
    private GoogleApiClient googleApiClient;
    public static String drive_id;
    public static DriveId driveID;
    ProgressDialog ringProgressDialog;
    ArrayList<SMSDataSer> smsBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_drive);
        smsBackup=(ArrayList<SMSDataSer>) getIntent().getSerializableExtra("backupdata");

        System.out.println(smsBackup.size());

        if (ContextCompat.checkSelfPermission(BackupDrive.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(BackupDrive.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);



        }
        else
        {
            fieWrite();
        }






        /*textFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Download" + File.separator + "backup.txt");

        try {
            FileWriter writer = new FileWriter(textFile);
            for(int k=0;k<smsBackup.size();k++)
            {
                writer.write(smsBackup.get(k).toString());

            }
            writer.close();


        }
        catch (Exception e)
        {


        }*/
            buildGoogleApiClient();



    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    this.fieWrite();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(BackupDrive.this, "No Permission Available", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "In onStart() - connecting...");
        googleApiClient.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null) {
            Log.i(TAG, "In onStop() - disConnecting...");
            googleApiClient.disconnect();
        }
    }
    public void fieWrite()
    {
        textFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Download" + File.separator + "backup.txt");

        try {
            FileOutputStream f = new FileOutputStream(textFile);
            PrintWriter pw = new PrintWriter(f);
            for(int k=0;k<smsBackup.size();k++)
            {
                pw.println(smsBackup.get(k).toString());

            }
            pw.flush();
            pw.close();
            f.close();
        } catch (Exception e) {

            System.out.println(e);
        }



    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "in onConnected() - we're connected, let's do the work in the background...");
        Drive.DriveApi.newDriveContents(googleApiClient)
                .setResultCallback(driveContentsCallback);
    }
    @Override
    public void onConnectionSuspended(int cause) {
        switch (cause) {
            case 1:
                Log.i(TAG, "Connection suspended - Cause: " + "Service disconnected");
                break;
            case 2:
                Log.i(TAG, "Connection suspended - Cause: " + "Connection lost");
                break;
            default:
                Log.i(TAG, "Connection suspended - Cause: " + "Unknown");
                break;
        }
    }
    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.i(TAG, "Error creating new file contents");
                        return;
                    }
                    final DriveContents driveContents = result.getDriveContents();
                    ringProgressDialog = ProgressDialog.show(BackupDrive.this, "Please wait ...", "BackingUp....", true);
                    ringProgressDialog.setCancelable(false);
                    ringProgressDialog.setCanceledOnTouchOutside(false);
                    new Thread() {
                        @Override
                        public void run() {
                            OutputStream outputStream = driveContents.getOutputStream();
                            addTextfileToOutputStream(outputStream);
                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle("backup")
                                    .setMimeType("text/plain")
                                    .setDescription("This is a text file uploaded from device")
                                    .setStarred(true).build();
                            Drive.DriveApi.getRootFolder(googleApiClient)
                                    .createFile(googleApiClient, changeSet, driveContents)
                                    .setResultCallback(fileCallback);
                        }
                    }.start();
                }
            };
    private void addTextfileToOutputStream(OutputStream outputStream) {
        Log.i(TAG, "adding text file to outputstream...");
        byte[] buffer = new byte[1024];
        int bytesRead;
        try {
            BufferedInputStream inputStream = new BufferedInputStream(
                    new FileInputStream(textFile));
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            Log.i(TAG, "problem converting input stream to output stream: " + e);
            e.printStackTrace();
        }
    }
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.i(TAG, "Error creating the file");
                        Toast.makeText(BackupDrive.this,
                                "Error adding file to Drive", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.i(TAG, "File added to Drive");
                    ringProgressDialog.dismiss();
                    Log.i(TAG, "Created a file with content: "
                            + result.getDriveFile().getDriveId());
                    Toast.makeText(BackupDrive.this,
                            "File successfully added to Drive", Toast.LENGTH_SHORT).show();
                    final PendingResult<DriveResource.MetadataResult> metadata
                            = result.getDriveFile().getMetadata(googleApiClient);
                    metadata.setResultCallback(new
                                                       ResultCallback<DriveResource.MetadataResult>() {
                                                           @Override
                                                           public void onResult(DriveResource.MetadataResult metadataResult) {

                                                               Metadata data = metadataResult.getMetadata();
                                                               Log.i(TAG, "Title: " + data.getTitle());
                                                               drive_id = data.getDriveId().encodeToString();
                                                               Log.i(TAG, "DrivId: " + drive_id);
                                                               driveID = data.getDriveId();
                                                               Log.i(TAG, "Description: " + data.getDescription().toString());
                                                               Log.i(TAG, "MimeType: " + data.getMimeType());
                                                               Log.i(TAG, "File size: " + String.valueOf(data.getFileSize()));
                                                           }
                                                       });

                    setResult(RESULT_OK);
                    finish();
                }
            };

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed");
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }
        try {
            Log.i(TAG, "trying to resolve the Connection failed error...");
            result.startResolutionForResult(this, REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }
    private void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Log.i(TAG, "In onActivityResult() - connecting...");
            googleApiClient.connect();
        }
    }
}
