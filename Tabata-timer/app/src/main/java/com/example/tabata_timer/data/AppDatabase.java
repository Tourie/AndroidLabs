package com.example.tabata_timer.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tabata_timer.models.Sequence;
import com.example.tabata_timer.models.WorkActivity;

@Database(entities = {Sequence.class, WorkActivity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WorkActivityDao workActivityDao();
    public abstract SequenceDao sequenceDao();
}
