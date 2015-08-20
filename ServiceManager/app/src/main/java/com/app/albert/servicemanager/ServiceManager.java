package com.app.albert.servicemanager;

/**
 * Created by albert on 15/8/19.
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import android.os.Handler;
import android.content.Context;
import android.app.ActivityManager;
import android.os.Build;
import android.content.ComponentName;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

public class ServiceManager extends Service
{
    private Context mContext;
    private Handler mHandler;
    private ActivityManager mActivityManager;
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
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        mContext = this;
        mHandler = new Handler();
        mHandler.postDelayed(CheckService, 1000);

        Toast.makeText(getApplicationContext(), "ServiceManager OnCreate", Toast.LENGTH_SHORT).show();
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
        mHandler.removeCallbacks(CheckService);
    }

    private Runnable CheckService = new Runnable() {
        public void run()
        {
            if(CheckAPP("com.app.albert.behaviorservice") == false)
            {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.app.albert.behaviorservice");
                if(launchIntent != null)
                {
                    startActivity(launchIntent);

                }
            }
            mHandler.postDelayed(CheckService, 1000);
        }
    };

    private boolean CheckAPP(String packageName)
    {
        //android.os.Debug.waitForDebugger();
        String[] activePackages;
        boolean isLaunched = false;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH)
        {
            activePackages = getActivePackages();
        }
        else
        {
            activePackages = getActivePackagesCompat();
        }

        if (activePackages != null)
        {
            for (String activePackage : activePackages)
            {
                if (activePackage.equals(packageName))
                {
                    //app is launched, do something
                    isLaunched = true;
                    break;
                }
            }
        }
        return isLaunched;
    }

    private String[] getActivePackagesCompat()
    {
        final List<ActivityManager.RunningTaskInfo> taskInfo = mActivityManager.getRunningTasks(1);
        final ComponentName componentName = taskInfo.get(0).topActivity;
        final String[] activePackages = new String[1];
        activePackages[0] = componentName.getPackageName();
        return activePackages;
    }

    private String[] getActivePackages()
    {
        final Set<String> activePackages = new HashSet<String>();
        final List<ActivityManager.RunningAppProcessInfo> processInfos = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos)
        {
            activePackages.addAll(Arrays.asList(processInfo.pkgList));
        }
        return activePackages.toArray(new String[activePackages.size()]);
    }
}