package com.example.tripplanner.data.repository;

import com.example.tripplanner.data.local.dao.TripDao;
import com.example.tripplanner.data.local.db.AppDatabase;
import com.example.tripplanner.data.local.entity.TripEntity;

import java.util.List;

/**
 * Repositorio de viajes. Delega en TripDao.
 */
public class TripRepository {

    private final TripDao tripDao;

    public TripRepository(AppDatabase database) {
        this.tripDao = database.tripDao();
    }

    public long insert(TripEntity trip) {
        return tripDao.insert(trip);
    }

    public int update(TripEntity trip) {
        return tripDao.update(trip);
    }

    public int delete(TripEntity trip) {
        return tripDao.delete(trip);
    }

    public List<TripEntity> getTripsByUserId(int userId) {
        return tripDao.getTripsByUserId(userId);
    }

    public TripEntity getTripById(long tripId) {
        return tripDao.getTripById(tripId);
    }
}
