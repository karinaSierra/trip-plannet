package com.example.tripplanner.ui.triplist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripplanner.R;
import com.example.tripplanner.data.local.entity.TripEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter para la lista de viajes en RecyclerView.
 */
public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<TripEntity> trips = new ArrayList<>();
    private OnTripActionListener actionListener;
    private OnTripClickListener clickListener;

    public interface OnTripActionListener {
        void onDeleteTrip(TripEntity trip);
    }

    public interface OnTripClickListener {
        void onTripClick(TripEntity trip);
    }

    public void setOnTripActionListener(OnTripActionListener listener) {
        this.actionListener = listener;
    }

    public void setOnTripClickListener(OnTripClickListener listener) {
        this.clickListener = listener;
    }

    public void setTrips(List<TripEntity> trips) {
        this.trips = trips != null ? trips : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripEntity trip = trips.get(position);
        holder.tvTitle.setText(trip.getTitle());
        holder.tvDestination.setText(trip.getDestination() != null ? trip.getDestination() : "");
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onTripClick(trip);
            }
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteTrip(trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final TextView tvDestination;
        final Button btnDelete;

        TripViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTripTitle);
            tvDestination = itemView.findViewById(R.id.tvTripDestination);
            btnDelete = itemView.findViewById(R.id.btnDeleteTrip);
        }
    }
}
