package com.example.tripplanner.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tripplanner.data.local.entity.TripEntity;

import java.util.List;

/**
 * DAO para operaciones sobre la tabla trips.
 */
@Dao
public interface TripDao {

    @Insert
    long insert(TripEntity trip);

    @Update
    int update(TripEntity trip);

    @Delete
    int delete(TripEntity trip);

    @Query("SELECT * FROM trips WHERE user_id = :userId ORDER BY start_date DESC")
    List<TripEntity> getTripsByUserId(int userId);
}
