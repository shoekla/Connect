package com.example.abirshukla.connect;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyService extends Service {
    String newR = "";
    ArrayList<String> files = new ArrayList<>();
    ArrayList<String> rFiles = new ArrayList<>();
    public MyService() {
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                final String fValues = "";
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String rVals = (String) dataSnapshot.child("RetrievedFiles").getValue().toString();
                        String value = (String) dataSnapshot.child("GivenFiles").getValue().toString();
                        if (value != null && rVals != null) {
                            System.out.println("Abir: " + rVals + ", " + value);
                            value = value.substring(value.indexOf("=") + 2, value.length() - 2);

                            String rArray[] = rVals.split(",");
                            for (int i = 0; i < rArray.length; i++) {
                                rArray[i] = rArray[i].trim();
                            }
                            for (int i = 0; i < rArray.length; i++) {
                                try {
                                    newR = newR + ",\"" + rArray[i].substring(1, rArray[i].length() - 1) + "\"";
                                    rFiles.add(rArray[i].substring(1, rArray[i].length() - 1));
                                } catch (Exception e) {

                                }
                            }
                            String array[] = value.split(",");
                            for (int i = 0; i < array.length; i++) {
                                //System.out.println("Array: "+array[i]);
                                array[i] = array[i].trim();
                                //System.out.println("Array: "+array[i]);
                            }
                            for (int i = 0; i < array.length; i++) {
                                if (!rFiles.contains(array[i].substring(1, array[i].length() - 1))) {
                                    files.add(array[i].substring(1, array[i].length() - 1));
                                }
                            }
                            System.out.println("Abir: " + files.size());
                            System.out.println("Abir: " + newR);
                            if (files.size() > 0) {
                                for (int i = 0; i < files.size(); i++) {
                                    newR = newR + ",\"" + files.get(i) + "\"";
                                    rFiles.add(files.get(i));
                                    downloadFileFromComputer(files.get(i));
                                }
                                newR = newR.substring(1);
                                System.out.println("NewR: " + newR);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("RetrievedFiles");

                                myRef.setValue(newR);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
                handler.postDelayed(this, 2000);
            }
        }, 2000);


    }
    public void downloadFileFromComputer(final String fileName) {
        StorageReference mstorageRef;
        mstorageRef = FirebaseStorage.getInstance().getReference();
        //StorageReference islandRef = mStorageRef.child("Kill Bill Vol. 2 OST - L'Arena - Ennio Morricone.mp3");
        mstorageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(fileName);
// in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

// get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);






            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {


        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}