package com.example.tabata_timer.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tabata_timer.models.Sequence;

import java.util.List;

@Dao
public interface SequenceDao {
    @Query("SELECT * FROM Sequence")
    List<Sequence> getAll();

    @Query("SELECT * FROM Sequence")
    LiveData<List<Sequence>> getAllLive();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Sequence workActivity);

    @Update
    void update(Sequence workActivity);

    @Delete
    void delete(Sequence workActivity);
}
