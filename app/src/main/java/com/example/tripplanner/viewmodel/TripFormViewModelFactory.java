package com.example.tripplanner.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripplanner.data.repository.TripRepository;

/**
 * Factory para TripFormViewModel con TripRepository y userId.
 */
public class TripFormViewModelFactory implements ViewModelProvider.Factory {

    private final TripRepository tripRepository;
    private final int userId;

    public TripFormViewModelFactory(TripRepository tripRepository, int userId) {
        this.tripRepository = tripRepository;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TripFormViewModel.class)) {
            @SuppressWarnings("unchecked")
            T result = (T) new TripFormViewModel(tripRepository, userId);
            return result;
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
