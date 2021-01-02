package com.example.tabata_timer.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tabata_timer.models.WorkActivity;

import java.util.List;

@Dao
public interface WorkActivityDao {

    @Query("SELECT * FROM workactivity")
    LiveData<List<WorkActivity>> getAllWorkActivities();

    @Query("SELECT * FROM WorkActivity WHERE :id == seq_id")
    List<WorkActivity> getSequenceActivities(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WorkActivity workActivity);

    @Update
    void update(WorkActivity workActivity);

    @Delete
    void delete(WorkActivity workActivity);

}
