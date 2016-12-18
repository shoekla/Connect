package com.example.abirshukla.connect;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListForDownload extends AppCompatActivity {
    ArrayList<String> files = new ArrayList<>();
    ArrayList<String> rFiles = new ArrayList<>();
    String newR = "";
    String values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_for_download);
        Bundle vals = getIntent().getExtras();
        values = vals.getString("values");
        String array[] = values.split(",");
        String rVals = vals.getString("rValues");
        String rArray[] = rVals.split(",");
        for (int i = 0; i < rArray.length; i++) {
            rArray[i] = rArray[i].trim();
        }
        for (int i = 0; i < rArray.length;i++) {
            try {
                newR = newR + ",\"" + rArray[i].substring(1, rArray[i].length() - 1) + "\"";
                rFiles.add(rArray[i].substring(1, rArray[i].length() - 1));
            }
            catch (Exception e) {

            }
        }
        for (int i = 0; i < array.length;i++) {
            //System.out.println("Array: "+array[i]);
            array[i] = array[i].trim();
            //System.out.println("Array: "+array[i]);
        }
        for (int i = 0; i < array.length;i++) {
            if (!rFiles.contains(array[i].substring(1,array[i].length()-1))) {
                files.add(array[i].substring(1, array[i].length() - 1));
            }
        }

        //System.out.println("Array: "+getFileType(files.get(0)));
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_listview,files);
        ListView listView = (ListView) findViewById(R.id.listview1);
        listView.setAdapter(adapter);
        //Toast.makeText(MainActivity.this, "Downloading",
         //       Toast.LENGTH_LONG).show();
    }
    public String getFileType(String name) {
        String type = name.substring(name.lastIndexOf(".")+1);
        return type;
    }
    public void allFiles(View view) {
        Intent a = new Intent(ListForDownload.this,AllFiles.class);
        a.putExtra("values",values);
        startActivity(a);
    }
    public void down (View view) {
        if (files.size() == 0) {
            Toast.makeText(ListForDownload.this, "Nothing To Donwload", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(ListForDownload.this, "Downloading", Toast.LENGTH_LONG).show();
        for (int i = 0; i < files.size();i++) {
            newR = newR+",\""+files.get(i)+"\"";
            rFiles.add(files.get(i));
            downloadFileFromComputer(files.get(i));
        }
        newR = newR.substring(1);
        System.out.println("NewR: "+newR);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RetrievedFiles");

        myRef.setValue(values);


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
                Toast.makeText(ListForDownload.this, "Error Occured",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
