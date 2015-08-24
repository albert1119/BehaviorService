package com.app.albert.behaviorservice;

/**
 * Created by albert on 15/8/19.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

public class BehaviorService extends Service
{
    public static enum State {Idle, GoingToCharging, Charging}
    public static enum Emotion {Default, Pleasure, Helpless, Lazy, Notice, Tired, Confidence, Proud}
    private State mState;
    private Emotion mEmotion;
    private Context mContext;
    private Handler mHandler;
    private final int[] mIdleWaitTime = {0, 20, 40, 60 };
    private final Emotion[] mIdleEmontions = { Emotion.Pleasure, Emotion.Helpless, Emotion.Lazy, Emotion.Notice };
    private final Emotion[] mChargingEmontions = { Emotion.Lazy, Emotion.Tired, Emotion.Default, Emotion.Confidence, Emotion.Proud };
    private int mIdleCheckCounter;
    private int mIdleEmotionIndex;
    private int mChargingLevel;
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
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        mState = State.Idle;
        mEmotion = Emotion.Default;
        mIdleCheckCounter = 0;
        mIdleEmotionIndex = 0;
        mChargingLevel = 0;
        mContext = this;
        mHandler = new Handler();
        mHandler.postDelayed(CheckState, 10 * 1000);
        Toast.makeText(this, "BehaviorService OnCreate", Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver mBroadcastReceiver =  new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // TODO Auto-generated method stub
            //android.os.Debug.waitForDebugger();
            String action = intent.getAction();

            if(action.equals(Intent.ACTION_POWER_CONNECTED))
            {
                // Do something when power connected
                mState = State.Charging;
                mIdleEmotionIndex = 0;
                mIdleCheckCounter = 0;
                mHandler.removeCallbacks(CheckState);
                mHandler.postDelayed(CheckState, 1 * 1000);
            }
            else if(action.equals(Intent.ACTION_POWER_DISCONNECTED))
            {
                // Do something when power disconnected
                mState = State.Idle;
                mIdleEmotionIndex = 0;
                mIdleCheckCounter = 0;
                mHandler.removeCallbacks(CheckState);
                mHandler.postDelayed(CheckState, 1 * 1000);
            }
            else if(action.equals(Intent.ACTION_BATTERY_CHANGED))
            {
                // Do something when power disconnected
                mChargingLevel = intent.getIntExtra("level",0);
            }

        }
    };

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
        unregisterReceiver(mBroadcastReceiver);
    }

    private Runnable CheckState = new Runnable() {
        public void run()
        {
            switch (mState)
            {
                case Idle:
                    if(mIdleCheckCounter <=0)
                    {
                        Toast.makeText(mContext, "State - Idle", Toast.LENGTH_SHORT).show();
                    }
                    Idle();
                    mHandler.postDelayed(CheckState, 1 * 1000);
                    break;
                case GoingToCharging:
                    mHandler.postDelayed(CheckState, 30 * 1000);
                    Toast.makeText(mContext, "State - GoingToCharging", Toast.LENGTH_SHORT).show();
                    break;
                case Charging:
                    Charging();
                    Toast.makeText(mContext, "State - Charging", Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(CheckState, 60 * 1000);
                    break;
            }
        }
    };

    private void Idle()
    {
        if(mIdleEmotionIndex >= mIdleWaitTime.length)
        {
            GotoCharge();
        }
        else
        {
            if (mIdleCheckCounter >= mIdleWaitTime[mIdleEmotionIndex] && mEmotion != mIdleEmontions[mIdleEmotionIndex])
            {
                SetEmotion(mIdleEmontions[mIdleEmotionIndex]);
                mIdleEmotionIndex++;
            }
            mIdleCheckCounter++;
        }
    }

    private void Charging()
    {
        if(mState == State.Charging)
        {
            if(mChargingLevel <= 50 && mEmotion != mChargingEmontions[0])
            {
                SetEmotion(mChargingEmontions[0]);
            }
            else if(mChargingLevel >=51 && mChargingLevel <= 75 && mEmotion != mChargingEmontions[1])
            {
                SetEmotion(mChargingEmontions[1]);
            }
            else if(mChargingLevel >=76 && mChargingLevel <= 95 && mEmotion != mChargingEmontions[2])
            {
                SetEmotion(mChargingEmontions[2]);
            }
            else if(mChargingLevel >=96 && mChargingLevel <= 99 && mEmotion != mChargingEmontions[3])
            {
                SetEmotion(mChargingEmontions[3]);
            }
            else if(mChargingLevel == 100 && mEmotion != mChargingEmontions[4])
            {
                SetEmotion(mChargingEmontions[4]);
            }

        }
    }

    private void SetEmotion(Emotion emotionId)
    {
        switch (emotionId)
        {
            case Pleasure:
                Toast.makeText(this, "Emotion Change to Pleasure", Toast.LENGTH_SHORT).show();
                break;
            case Helpless:
                Toast.makeText(this, "Emotion Change to Helpless", Toast.LENGTH_SHORT).show();
                break;
            case Lazy:
                Toast.makeText(this, "Emotion Change to Lazy", Toast.LENGTH_SHORT).show();
                break;
            case Notice:
                Toast.makeText(this, "Emotion Change to Notice", Toast.LENGTH_SHORT).show();
                break;
            case Tired:
                Toast.makeText(this, "Emotion Change to Tired", Toast.LENGTH_SHORT).show();
                break;
            case Confidence:
                Toast.makeText(this, "Emotion Change to Confidence", Toast.LENGTH_SHORT).show();
                break;
            case Proud:
                Toast.makeText(this, "Emotion Change to Proud", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Emotion Change to Default", Toast.LENGTH_SHORT).show();
        }
    }

    private void GotoCharge()
    {
        mHandler.removeCallbacks(CheckState);
        mIdleEmotionIndex = 0;
        mIdleCheckCounter = 0;
        mState = State.GoingToCharging;
        Toast.makeText(this, "Going to Charge", Toast.LENGTH_SHORT).show();
    }

    private void NoticeSomeone()
    {
        if(mState == State.Idle)
        {
            mHandler.removeCallbacks(CheckState);
            SetEmotion(Emotion.Notice);
        }
    }

    private void SomeoneLeave()
    {
        if(mState == State.Idle)
        {
            mHandler.removeCallbacks(CheckState);
            Idle();
        }
    }
}