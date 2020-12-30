package com.example.tabata_timer.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Sequence implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "colour")
    public int colour;

    public Sequence(){
    }

    protected Sequence(Parcel in) {
        id = in.readInt();
        title = in.readString();
        colour = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(colour);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Sequence> CREATOR = new Creator<Sequence>() {
        @Override
        public Sequence createFromParcel(Parcel in) {
            return new Sequence(in);
        }

        @Override
        public Sequence[] newArray(int size) {
            return new Sequence[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sequence sequence = (Sequence) o;
        return id == sequence.id &&
                colour == sequence.colour &&
                title.equals(sequence.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, colour);
    }
}
