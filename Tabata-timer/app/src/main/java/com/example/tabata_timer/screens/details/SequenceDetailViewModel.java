package com.example.tabata_timer.screens.details;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tabata_timer.App;
import com.example.tabata_timer.models.Sequence;
import com.example.tabata_timer.models.WorkActivity;

import java.util.List;

public class SequenceDetailViewModel extends ViewModel {

    public List<WorkActivity> getWorkActivities(int id) {
        return App.getInstance().getWorkActivityDao().getSequenceActivities(id);
    }

    public LiveData<List<WorkActivity>> getWorkActivitiesLiveData() {
        return workActivitiesLiveData;
    }
    private LiveData<List<WorkActivity>> workActivitiesLiveData = App.getInstance().getWorkActivityDao().getAllWorkActivities();
}


