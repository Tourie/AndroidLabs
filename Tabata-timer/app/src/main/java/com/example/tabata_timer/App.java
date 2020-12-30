package com.example.tabata_timer;

import android.app.Application;
import android.content.ContentProvider;
import android.os.Build;

import androidx.room.Room;

import com.example.tabata_timer.data.AppDatabase;
import com.example.tabata_timer.data.SequenceDao;
import com.example.tabata_timer.data.WorkActivityDao;

public class App extends Application {

    private AppDatabase db;
    private SequenceDao sequenceDao;
    private WorkActivityDao workActivityDao;

    private static App instance;

    public static App getInstance(){
        return instance;
    }

    public SequenceDao getSequenceDao() {
        return sequenceDao;
    }

    public WorkActivityDao getWorkActivityDao() {
        return workActivityDao;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        db = Room.databaseBuilder(getApplicationContext(),
            AppDatabase.class, "tabata-timer-db")
            .allowMainThreadQueries()
            .build();

        sequenceDao = db.sequenceDao();
        workActivityDao = db.workActivityDao();
    }
    public AppDatabase getDb(){
        return db;
    }
}
