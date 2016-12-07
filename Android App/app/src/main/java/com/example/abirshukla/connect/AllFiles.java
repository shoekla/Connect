package com.example.abirshukla.connect;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AllFiles extends AppCompatActivity {
    ArrayList<String> files = new ArrayList<>();

    String values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_files);
        Bundle vals = getIntent().getExtras();
        values = vals.getString("values");
        String array[] = values.split(",");
        for (int i = 0; i < array.length;i++) {
            //System.out.println("Array: "+array[i]);
            array[i] = array[i].trim();
            //System.out.println("Array: "+array[i]);
        }
        for (int i = 0; i < array.length;i++) {
            files.add(array[i].substring(1, array[i].length() - 1));
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_listview,files);
        ListView listView = (ListView) findViewById(R.id.listview1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                downloadFileFromComputer(files.get(position));


            }
        });
        Toast.makeText(AllFiles.this, "Click Files You Want To Download", Toast.LENGTH_LONG).show();
    }


    public void downloadFileFromComputer(final String fileName) {
        Toast.makeText(AllFiles.this, "Downloading "+fileName, Toast.LENGTH_LONG).show();
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
                Toast.makeText(AllFiles.this, "Error Occured",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}
