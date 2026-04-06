package com.example.tripplanner.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripplanner.data.repository.ItemRepository;
import com.example.tripplanner.data.repository.TripRepository;

/**
 * Factory para TripDetailViewModel con TripRepository.
 */
public class TripDetailViewModelFactory implements ViewModelProvider.Factory {

    private final TripRepository tripRepository;
    private final ItemRepository itemRepository;

    public TripDetailViewModelFactory(TripRepository tripRepository, ItemRepository itemRepository) {
        this.tripRepository = tripRepository;
        this.itemRepository = itemRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TripDetailViewModel.class)) {
            @SuppressWarnings("unchecked")
            T result = (T) new TripDetailViewModel(tripRepository, itemRepository);
            return result;
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

