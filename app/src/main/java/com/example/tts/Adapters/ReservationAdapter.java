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

import com.example.tts.CurrentBookingSummaryActivity;
import com.example.tts.Model.Reservation;
import com.example.tts.R;

import java.util.List;


public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    private List<Reservation> itemList;
    private Context context;

    public ReservationAdapter(Context context, List<Reservation> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = itemList.get(position);
        holder.name.setText(reservation.getTrain().getName());
        holder.date.setText(reservation.getDate());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CurrentBookingSummaryActivity.class);
                intent.putExtra("reservationId",String.valueOf(reservation.getId()));
                intent.putExtra("trainId",String.valueOf(reservation.getTrain().getTrainNo()));
                intent.putExtra("date",String.valueOf(reservation.getDate()));
                view.getContext().startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,date;
        LinearLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.reservationTrainName);
            date = itemView.findViewById(R.id.reservationTrainDate);
            card = itemView.findViewById(R.id.reservationItem);

        }
    }


}
