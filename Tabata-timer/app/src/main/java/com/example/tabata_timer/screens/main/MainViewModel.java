package com.example.tabata_timer.screens.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tabata_timer.App;
import com.example.tabata_timer.models.Sequence;

import java.util.List;

public class MainViewModel extends ViewModel {
    public LiveData<List<Sequence>> getSequenceLiveData() {
        return sequenceLiveData;
    }

    private LiveData<List<Sequence>> sequenceLiveData = App.getInstance().getSequenceDao().getAllLive();
}
