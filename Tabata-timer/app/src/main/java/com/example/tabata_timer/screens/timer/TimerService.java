package com.example.tabata_timer.screens.timer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.tabata_timer.R;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private CountDownTimer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final long[] timeRemaining = {intent.getIntExtra(TimerActivity.TIME_VALUE_KEY, 0)*1000};
        Intent localIntent = new Intent();
        localIntent.setAction("Counter");
        timer = new CountDownTimer(timeRemaining[0], 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining[0] = millisUntilFinished;
                localIntent.putExtra("TimeRemaining", timeRemaining[0]/1000);
                sendBroadcast(localIntent);
            }

            @Override
            public void onFinish() {
                localIntent.putExtra("TimerFinished", 0);
                sendBroadcast(localIntent);
            }
        }.start();

        IntentFilter pauseIntentFilter = new IntentFilter();
        pauseIntentFilter.addAction("TimerSetPause");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                OnPause();
            }
        };

        registerReceiver(broadcastReceiver, pauseIntentFilter);

        return super.onStartCommand(intent, flags, startId);
    }

    public void OnPause() {
        timer.cancel();
    }
}