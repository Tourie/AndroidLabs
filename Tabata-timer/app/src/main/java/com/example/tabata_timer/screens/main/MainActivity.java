package com.example.tabata_timer.screens.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tabata_timer.App;
import com.example.tabata_timer.R;
import com.example.tabata_timer.models.Sequence;
import com.example.tabata_timer.screens.settings.SettingsActivity;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsActivity.SetConfigurations(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        SequenceAdapter sequenceAdapter = new SequenceAdapter();
        recyclerView.setAdapter(sequenceAdapter);

        Button add = findViewById(R.id.add_new_sequence);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sequence sequence = new Sequence();
                Random randomGenerator = new Random();
                int red = randomGenerator.nextInt(256);
                int green = randomGenerator.nextInt(256);
                int blue = randomGenerator.nextInt(256);
                sequence.colour = android.graphics.Color.argb(255, red, green, blue);
                sequence.title = "Новая последовательность";
                App.getInstance().getSequenceDao().insert(sequence);
            }
        });

        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getSequenceLiveData().observe(this, new Observer<List<Sequence>>() {
            @Override
            public void onChanged(List<Sequence> sequences) {
                sequenceAdapter.setItems(sequences);
            }
        });
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
    }
}