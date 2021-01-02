package com.example.tabata_timer.screens.timer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.tabata_timer.R;
import com.example.tabata_timer.models.WorkActivity;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder> {

    SortedList<WorkActivity> workActivitySortedList;
    public Adapter(){
        workActivitySortedList = new SortedList<>(WorkActivity.class, new SortedList.Callback<WorkActivity>() {
            @Override
            public int compare(WorkActivity o1, WorkActivity o2) {
                return 0;
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemChanged(position,count);
            }

            @Override
            public boolean areContentsTheSame(WorkActivity oldItem, WorkActivity newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(WorkActivity item1, WorkActivity item2) {
                return item1.id == item2.id;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position,count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position,count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition,toPosition);
            }
        });
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Adapter.AdapterViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timer_work_activity_list,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        holder.bind(workActivitySortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return workActivitySortedList.size();
    }

    public void setItems(List<WorkActivity> workActivities){
        workActivitySortedList.replaceAll(workActivities);
    }

    static class AdapterViewHolder extends RecyclerView.ViewHolder{

        TextView work_activity_title;
        TextView time;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            work_activity_title = itemView.findViewById(R.id.work_activity_title);
            time = itemView.findViewById(R.id.time);
        }

        public void bind(WorkActivity workActivity){
            work_activity_title.setText(workActivity.title);
            time.setText(String.valueOf(workActivity.duration));
        }
    }
}
