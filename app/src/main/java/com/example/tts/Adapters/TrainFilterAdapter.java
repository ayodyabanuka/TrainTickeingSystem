package com.example.tts.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tts.Model.Train;
import com.example.tts.PastBookingSummaryActivity;
import com.example.tts.R;

import java.util.List;

public class TrainFilterAdapter extends RecyclerView.Adapter<TrainFilterAdapter.ViewHolder> {

    private List<Train> trainList;
    private Context context;

    public TrainFilterAdapter(Context context, List<Train> trainList) {
        this.context = context;
        this.trainList = trainList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.train_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Train train = trainList.get(position);
        holder.name.setText(train.getName());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PastBookingSummaryActivity.class);
                intent.putExtra("TrainNo",train.getTrainNo());
                view.getContext().startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return trainList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.TrainName);
            card = itemView.findViewById(R.id.trainItem);

        }
    }
}
