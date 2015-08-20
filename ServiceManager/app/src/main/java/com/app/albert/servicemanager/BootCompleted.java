package com.app.albert.servicemanager;

/**
 * Created by albert on 15/8/19.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        //we double check here for only boot complete event
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
        {
            //here we start the service
            Intent serviceIntent = new Intent(context, ServiceManager.class);
            context.startService(serviceIntent);
        }

    }

}
