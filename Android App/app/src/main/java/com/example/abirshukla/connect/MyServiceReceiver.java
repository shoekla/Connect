package com.example.abirshukla.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by abirshukla on 12/6/16.
 */
public class MyServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        System.out.println("Run");
        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();

    }

}
