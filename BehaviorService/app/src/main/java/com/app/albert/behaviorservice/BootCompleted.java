package com.app.albert.behaviorservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Created by albert on 15/8/19.
 */
public class BootCompleted extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        //we double check here for only boot complete event
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
        {
            //here we start the service
            Intent serviceIntent = new Intent(context, BehaviorService.class);
            context.startService(serviceIntent);
            Intent serviceIntent2 = new Intent(context, PoliceService.class);
            context.startService(serviceIntent2);
        }

    }

}
