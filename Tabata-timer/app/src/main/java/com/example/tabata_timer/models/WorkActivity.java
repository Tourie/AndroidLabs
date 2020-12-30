package com.example.tabata_timer.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Sequence.class, parentColumns = "id", childColumns = "seq_id", onDelete = CASCADE))
public class WorkActivity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "duration")
    public int duration;

    @ColumnInfo(name = "seq_id")
    public int seq_id;

    public WorkActivity(){
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkActivity that = (WorkActivity) o;
        return id == that.id &&
                duration == that.duration &&
                seq_id == that.seq_id &&
                title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, duration, seq_id);
    }
}
