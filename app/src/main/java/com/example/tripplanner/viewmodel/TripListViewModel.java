package com.example.tripplanner.viewmodel;

import com.example.tripplanner.data.local.entity.TripEntity;
import com.example.tripplanner.data.repository.TripRepository;
import com.example.tripplanner.viewmodel.base.BaseViewModel;

import java.util.List;

/**
 * ViewModel para la lista de viajes. Delega en TripRepository.
 */
public class TripListViewModel extends BaseViewModel {

    private final TripRepository tripRepository;

    public TripListViewModel(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<TripEntity> getTripsByUserId(int userId) {
        return tripRepository.getTripsByUserId(userId);
    }

    public long insert(TripEntity trip) {
        return tripRepository.insert(trip);
    }

    public int delete(TripEntity trip) {
        return tripRepository.delete(trip);
    }
}
