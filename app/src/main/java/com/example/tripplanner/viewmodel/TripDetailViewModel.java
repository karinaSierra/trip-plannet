package com.example.tripplanner.viewmodel;

import com.example.tripplanner.data.local.entity.TripEntity;
import com.example.tripplanner.data.repository.TripRepository;
import com.example.tripplanner.viewmodel.base.BaseViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ViewModel para el detalle de un viaje concreto.
 */
public class TripDetailViewModel extends BaseViewModel {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private final TripRepository tripRepository;

    public TripDetailViewModel(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public TripEntity getTripById(long tripId) {
        return tripRepository.getTripById(tripId);
    }

    public int deleteTrip(TripEntity trip) {
        return tripRepository.delete(trip);
    }

    public String formatDate(long timestamp) {
        if (timestamp <= 0) return "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            return sdf.format(new Date(timestamp));
        } catch (Exception e) {
            return "";
        }
    }
}

