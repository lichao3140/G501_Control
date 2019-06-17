package com.lichao.g501_control;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.common.pos.api.util.PosUtil401;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ProximityService extends Service {

    private Timer mTimer = null;
    private TimerTask mTask = null;
    private static final String TAG = "WINDOW";

    Calendar cal;
    String year;
    String month;
    String day;
    String hour;
    String minute;
    String second;
    String my_time_1;
    String my_time_2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler showHandler = new Handler(Looper.getMainLooper());

    private void show(final String message) {
        showHandler.post(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mTask == null) {
            Log.i(TAG, "chuangjian mTask");
            mTask = new TimerTask() {
                @Override
                public void run() {
                    int ret = PosUtil401.getPriximitySensorStatus();
                    Message message = mHandler.obtainMessage(ret);
                    mHandler.sendMessage(message);
                }
            };
        }

        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(mTask, 0, 200);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    MainActivity.setSensorText("people in");
                    break;
                case 0:
                    MainActivity.setSensorText("people out");
                    break;
                default:
                    show("error");
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTask = null;
        mTimer = null;
        Log.i(TAG, "service is onDestroy");
    }
}
