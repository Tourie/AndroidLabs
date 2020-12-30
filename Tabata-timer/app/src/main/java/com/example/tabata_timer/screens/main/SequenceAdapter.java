package com.example.tabata_timer.screens.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.tabata_timer.App;
import com.example.tabata_timer.R;
import com.example.tabata_timer.models.Sequence;
import com.example.tabata_timer.screens.details.SequenceDetailActivity;

import java.util.List;

public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.SequenceViewHolder> {

    SortedList<Sequence> sequenceSortedList;
    public SequenceAdapter(){
        sequenceSortedList = new SortedList<>(Sequence.class, new SortedList.Callback<Sequence>() {
            @Override
            public int compare(Sequence o1, Sequence o2) {
                return 0;
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemChanged(position,count);
            }

            @Override
            public boolean areContentsTheSame(Sequence oldItem, Sequence newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Sequence item1, Sequence item2) {
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
    public SequenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SequenceViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sequence_list,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SequenceViewHolder holder, int position) {
        holder.bind(sequenceSortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sequenceSortedList.size();
    }

    public void setItems(List<Sequence> sequences){
        sequenceSortedList.replaceAll(sequences);
    }

    static class SequenceViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        Button delete;

        Sequence sequence;


        public SequenceViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.sequence_title);
            delete = itemView.findViewById(R.id.sequence_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.getInstance().getSequenceDao().delete(sequence);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SequenceDetailActivity.start((Activity)itemView.getContext(), sequence);
                }
            });
        }

        public void bind(Sequence sequence){
            this.sequence = sequence;

            textView.setText(sequence.title);
            itemView.setBackgroundColor(sequence.colour);
        }

    }
}
