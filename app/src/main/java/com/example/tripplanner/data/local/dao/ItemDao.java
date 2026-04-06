package com.example.tripplanner.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tripplanner.data.local.entity.ItemEntity;

import java.util.List;

/**
 * DAO para operaciones sobre la tabla items.
 */
@Dao
public interface ItemDao {

    @Insert
    long insert(ItemEntity item);

    @Update
    int update(ItemEntity item);

    @Delete
    int delete(ItemEntity item);

    @Query("SELECT * FROM items WHERE trip_id = :tripId ORDER BY id ASC")
    List<ItemEntity> getItemsByTripId(long tripId);
}
