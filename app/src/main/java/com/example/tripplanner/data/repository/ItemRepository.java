package com.example.tripplanner.data.repository;

import com.example.tripplanner.data.local.dao.ItemDao;
import com.example.tripplanner.data.local.db.AppDatabase;
import com.example.tripplanner.data.local.entity.ItemEntity;

import java.util.List;

/**
 * Repositorio de ítems de viaje. Delega en ItemDao.
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

    public int delete(ItemEntity item) {
        return itemDao.delete(item);
    }

    public List<ItemEntity> getItemsByTripId(int tripId) {
        return itemDao.getItemsByTripId(tripId);
    }
}
