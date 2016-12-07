package com.example.abirshukla.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by abirshukla on 12/6/16.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {

        Toast.makeText(context, "Test", Toast.LENGTH_LONG).show();
        //do w/e
    }
}