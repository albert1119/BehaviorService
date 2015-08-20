package com.app.albert.behaviorservice;

/**
 * Created by albert on 15/8/19.
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class BehaviorService extends Service
{

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        // do something when the service is created
        //android.os.Debug.waitForDebugger();


        Toast.makeText(this, "BehaviorService OnCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //android.os.Debug.waitForDebugger();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}