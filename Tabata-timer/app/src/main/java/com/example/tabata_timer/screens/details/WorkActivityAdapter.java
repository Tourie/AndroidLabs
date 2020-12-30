package com.example.tabata_timer.screens.details;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.tabata_timer.App;
import com.example.tabata_timer.R;
import com.example.tabata_timer.models.Sequence;
import com.example.tabata_timer.models.WorkActivity;
import com.example.tabata_timer.screens.main.SequenceAdapter;

import java.util.List;

public class WorkActivityAdapter extends RecyclerView.Adapter<WorkActivityAdapter.WorkActivityViewHolder> {

    SortedList<WorkActivity> workActivitySortedList;
    public WorkActivityAdapter(){
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
    public WorkActivityAdapter.WorkActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkActivityAdapter.WorkActivityViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_activity_list,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull WorkActivityAdapter.WorkActivityViewHolder holder, int position) {
        holder.bind(workActivitySortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return workActivitySortedList.size();
    }

    public void setItems(List<WorkActivity> workActivities){
        workActivitySortedList.replaceAll(workActivities);
    }

    static class WorkActivityViewHolder extends RecyclerView.ViewHolder {

        EditText title;
        TextView time;
        Button minus;
        Button plus;
        Button delete;
        Button change;

        WorkActivity workActivity;


        public WorkActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.work_activity_title);
            time = itemView.findViewById(R.id.time);
            minus = itemView.findViewById(R.id.minus_btn);
            plus = itemView.findViewById(R.id.plus_btn);
            delete = itemView.findViewById(R.id.work_activity_delete);
            change = itemView.findViewById(R.id.work_activity_change);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.getInstance().getWorkActivityDao().delete(workActivity);
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(time.getText().toString());
                    if(num > 0){
                        num--;
                        time.setText(String.valueOf(num));
                    }
                }
            });

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(time.getText().toString());
                    num++;
                    time.setText(String.valueOf(num));
                }
            });
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workActivity.title = title.getText().toString();
                    workActivity.duration = Integer.parseInt(time.getText().toString());
                    App.getInstance().getWorkActivityDao().update(workActivity);
                }
            });

        }

        public void bind(WorkActivity workActivity){
            this.workActivity = workActivity;
            title.setText(workActivity.title);
            time.setText(String.valueOf(workActivity.duration));
        }

    }
}
