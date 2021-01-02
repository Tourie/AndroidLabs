package com.example.tabata_timer.screens.details;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tabata_timer.App;
import com.example.tabata_timer.R;
import com.example.tabata_timer.models.Sequence;
import com.example.tabata_timer.models.WorkActivity;
import com.example.tabata_timer.screens.settings.SettingsActivity;
import com.example.tabata_timer.screens.timer.TimerActivity;

public class SequenceDetailActivity extends AppCompatActivity {

    private static final String EXTRA_SEQUENCE = "SequenceDetailActivity";

    private RecyclerView recyclerView;
    private Sequence sequence;
    private EditText title;
    private Button saveButton;
    private Button addWorkActivity;

    public static void start(Activity caller, Sequence sequence){
        Intent intent = new Intent(caller, SequenceDetailActivity.class);
        if (sequence != null){
            intent.putExtra(EXTRA_SEQUENCE, sequence);
        }
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsActivity.SetConfigurations(this);
        setContentView(R.layout.activity_sequence_detail);
        title = findViewById(R.id.sequence_title);
        saveButton = findViewById(R.id.save_button);
        addWorkActivity = findViewById(R.id.addWorkActivity);

        if(getIntent().hasExtra(EXTRA_SEQUENCE)){
            sequence = getIntent().getParcelableExtra(EXTRA_SEQUENCE);
            title.setText(sequence.title);
        } else {
            sequence = new Sequence();
        }

        recyclerView = findViewById(R.id.work_activity_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        WorkActivityAdapter workActivityAdapter = new WorkActivityAdapter();
        recyclerView.setAdapter(workActivityAdapter);

        SequenceDetailViewModel sequenceDetailViewModel = new ViewModelProvider(this).get(SequenceDetailViewModel.class);
        sequenceDetailViewModel.getWorkActivitiesLiveData().observe(this, new Observer<List<WorkActivity>>() {
            @Override
            public void onChanged(List<WorkActivity> workActivities) {
                List<WorkActivity> new_w = sequenceDetailViewModel.getWorkActivities(sequence.id);
                workActivityAdapter.setItems(new_w);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sequence.title = title.getText().toString();
                if(getIntent().hasExtra(EXTRA_SEQUENCE)){
                    App.getInstance().getSequenceDao().update(sequence);
                }
                else {
                    //random color for sequences
                    Random randomGenerator = new Random();
                    int red = randomGenerator.nextInt(256);
                    int green = randomGenerator.nextInt(256);
                    int blue = randomGenerator.nextInt(256);
                    sequence.colour = android.graphics.Color.argb(80, red, green, blue);
                    App.getInstance().getSequenceDao().insert(sequence);
                }
                finish();
            }
        });
        addWorkActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkActivity workActivity = new WorkActivity();
                workActivity.duration = 1;
                workActivity.title = "Название активности";
                workActivity.seq_id = sequence.id;
                App.getInstance().getWorkActivityDao().insert(workActivity);
            }
        });
    }

    public void startTimerActivity(View view) {
        TimerActivity.startActivity(this,sequence);
    }
}