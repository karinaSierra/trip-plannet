package com.example.tripplanner.viewmodel;

import com.example.tripplanner.data.local.entity.TripEntity;
import com.example.tripplanner.data.local.entity.ItemEntity;
import com.example.tripplanner.data.repository.ItemRepository;
import com.example.tripplanner.data.repository.TripRepository;
import com.example.tripplanner.viewmodel.base.BaseViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ViewModel para el detalle de un viaje concreto.
 */
public class TripDetailViewModel extends BaseViewModel {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String[] DEFAULT_TASKS = new String[] {
            "Revisar pasaporte/identificación",
            "Reservar alojamiento",
            "Comprar boletos o transporte",
            "Preparar equipaje",
            "Verificar clima del destino"
    };

    private final TripRepository tripRepository;
    private final ItemRepository itemRepository;

    public TripDetailViewModel(TripRepository tripRepository, ItemRepository itemRepository) {
        this.tripRepository = tripRepository;
        this.itemRepository = itemRepository;
    }

    public TripEntity getTripById(long tripId) {
        return tripRepository.getTripById(tripId);
    }

    public int deleteTrip(TripEntity trip) {
        return tripRepository.delete(trip);
    }

    public List<ItemEntity> getItemsByTripId(long tripId) {
        List<ItemEntity> items = itemRepository.getItemsByTripId(tripId);
        return items != null ? items : new ArrayList<>();
    }

    public int updateItem(ItemEntity item) {
        return itemRepository.update(item);
    }

    /**
     * Crea una checklist inicial solo si el viaje no tiene items.
     * @return cantidad de items creados.
     */
    public int createDefaultChecklistIfEmpty(long tripId) {
        List<ItemEntity> existingItems = itemRepository.getItemsByTripId(tripId);
        if (existingItems != null && !existingItems.isEmpty()) {
            return 0;
        }
        int created = 0;
        for (String task : DEFAULT_TASKS) {
            ItemEntity item = new ItemEntity();
            item.setTripId(tripId);
            item.setName(task);
            item.setDescription("");
            item.setCompleted(false);
            long inserted = itemRepository.insert(item);
            if (inserted > 0) {
                created++;
            }
        }
        return created;
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

