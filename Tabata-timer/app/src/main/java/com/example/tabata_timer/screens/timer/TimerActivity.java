package com.example.tabata_timer.screens.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tabata_timer.App;
import com.example.tabata_timer.R;
import com.example.tabata_timer.models.Sequence;
import com.example.tabata_timer.models.WorkActivity;
import com.example.tabata_timer.screens.settings.SettingsActivity;

import java.util.List;

public class TimerActivity extends AppCompatActivity implements SoundPool.OnLoadCompleteListener {

    public static final String SEQUENCE_KEY = "SequenceObj";
    public static final String TIME_VALUE_KEY = "TimeValue";
    private static final int MAX_STREAMS = 5;
    private TextView timer_view;
    private SoundPool sp;
    int soundIdCompleted;
    private Sequence sequence;
    RecyclerView recyclerView;
    List<WorkActivity> workActivities;
    private boolean timerIsWorking;
    private boolean timerPaused;
    private int stopped_time;
    Button start_pause_button;
    BroadcastReceiver broadcastReceiver;
    TextView current_work_activity;

    public static void startActivity(Activity caller, Sequence sequence){
        Intent intent = new Intent(caller, TimerActivity.class);
        intent.putExtra(SEQUENCE_KEY, sequence);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsActivity.SetConfigurations(this);
        setContentView(R.layout.activity_timer);

        this.sequence = getIntent().getParcelableExtra(SEQUENCE_KEY);
        start_pause_button = findViewById(R.id.start_pause_button);
        current_work_activity = findViewById(R.id.current_work_activity);

        recyclerView = findViewById(R.id.list_of_next_work_activities);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        workActivities = App.getInstance().getWorkActivityDao().getSequenceActivities(sequence.id);
        adapter.setItems(workActivities);

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
        soundIdCompleted = sp.load(this, R.raw.carme, 1);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);
        timer_view = findViewById(R.id.timer_view);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Counter");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Long time = intent.getLongExtra("TimeRemaining",0);
                timer_view.setText(time.toString());
                if(intent.hasExtra("TimerFinished")){
                    sp.play(soundIdCompleted,1, 1, 0, 0, 1);
                    workActivities.remove(0);
                    adapter.setItems(workActivities);
                    startNewWorkActivity();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void startButton(View view){
        if(!timerIsWorking && !timerPaused){
            timerIsWorking = true;
            start_pause_button.setText(R.string.pause);
            startNewWorkActivity();
        } else {
            if(timerPaused){
                start_pause_button.setText(R.string.pause);
                timerPaused = false;
                Intent intentService = new Intent(this, TimerService.class);
                intentService.putExtra(TIME_VALUE_KEY, stopped_time);
                startService(intentService);
            } else{
                start_pause_button.setText(R.string.start);
                timerPaused = true;
                stopped_time = Integer.parseInt(timer_view.getText().toString());
                Intent localIntent = new Intent();
                localIntent.setAction("TimerSetPause");
                sendBroadcast(localIntent);
            }
        }
    }

    public void startNewWorkActivity(){
        if(workActivities.size()>0){
            Intent intentService = new Intent(this, TimerService.class);
            current_work_activity.setText(workActivities.get(0).title);
            intentService.putExtra(TIME_VALUE_KEY, workActivities.get(0).duration);
            startService(intentService);
        } else {
            timerIsWorking=false;
            unregisterReceiver(broadcastReceiver);
            finish();
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }

    public void nextButton(View view) {
        Intent localIntent = new Intent();
        localIntent.setAction("TimerSetPause");
        sendBroadcast(localIntent);
        localIntent.setAction("Counter");
        localIntent.putExtra("TimerFinished", true);
        sendBroadcast(localIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        workActivities.clear();
        unregisterReceiver(broadcastReceiver);
        finish();
    }
}