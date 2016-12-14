package com.example.abirshukla.connect;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView tv;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        Intent myIntent = new Intent(this, MyService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,  0, myIntent, 0);

        Calendar calendar = Calendar.getInstance();
        pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 2);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pendingIntent);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String rVals = (String) dataSnapshot.child("RetrievedFiles").getValue().toString();
                String value = (String) dataSnapshot.child("GivenFiles").getValue().toString();
                value = value.substring(value.indexOf("=")+2,value.length()-2);
                System.out.println("Value: "+value);
                System.out.println("Val: "+rVals);
                Intent d = new Intent(MainActivity.this,ListForDownload.class);
                d.putExtra("rValues",rVals);
                d.putExtra("values",value);
                startActivity(d);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }


}
