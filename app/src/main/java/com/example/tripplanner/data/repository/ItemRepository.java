package com.example.tripplanner.data.repository;

import com.example.tripplanner.data.local.dao.ItemDao;
import com.example.tripplanner.data.local.db.AppDatabase;
import com.example.tripplanner.data.local.entity.ItemEntity;

import java.util.List;

/**
 * Repositorio de items del checklist por viaje.
 */
public class ItemRepository {

    private final ItemDao itemDao;

    public ItemRepository(AppDatabase database) {
        this.itemDao = database.itemDao();
    }

    public long insert(ItemEntity item) {
        return itemDao.insert(item);
    }

    public int update(ItemEntity item) {
        return itemDao.update(item);
    }

    public List<ItemEntity> getItemsByTripId(long tripId) {
        return itemDao.getItemsByTripId(tripId);
    }
}
