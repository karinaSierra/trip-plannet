package com.example.tripplanner.viewmodel;

import com.example.tripplanner.data.local.entity.TripEntity;
import com.example.tripplanner.data.repository.TripRepository;
import com.example.tripplanner.viewmodel.base.BaseViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ViewModel para el formulario de creación/edición de viajes.
 */
public class TripFormViewModel extends BaseViewModel {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private final TripRepository tripRepository;
    private final int userId;

    /** Viaje en edición; null si es nuevo. */
    private TripEntity currentTrip;

    public TripFormViewModel(TripRepository tripRepository, int userId) {
        this.tripRepository = tripRepository;
        this.userId = userId;
    }

    /**
     * Carga un viaje por id para edición. Retorna null si no existe.
     */
    public TripEntity loadTrip(long tripId) {
        currentTrip = tripRepository.getTripById(tripId);
        return currentTrip;
    }

    /**
     * Formatea un timestamp a "dd/MM/yyyy" para mostrar en el formulario.
     */
    public String formatDate(long timestamp) {
        if (timestamp <= 0) return "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            return sdf.format(new Date(timestamp));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Parsea una fecha en formato dd/MM/yyyy a timestamp en milis.
     */
    public long parseDate(String dateStr) {
        return parseDateFlexible(dateStr);
    }

    /**
     * Intenta varios formatos habituales (evita fallos silenciosos al editar).
     */
    private long parseDateFlexible(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return 0L;
        }
        String s = dateStr.trim();
        String[] patterns = {
                DATE_FORMAT,
                "d/M/yyyy",
                "dd/M/yyyy",
                "d/MM/yyyy",
                "yyyy-MM-dd",
                "dd-MM-yyyy",
                "d-M-yyyy"
        };
        for (String pattern : patterns) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
                sdf.setLenient(false);
                return sdf.parse(s).getTime();
            } catch (Exception ignored) {
                // siguiente patrón
            }
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            sdf.setLenient(true);
            return sdf.parse(s).getTime();
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * En edición: si el campo está vacío o el texto no se puede parsear, conserva la fecha anterior.
     * Así no se pierden fechas al fallar el parseo o al dejar el campo en blanco por error.
     */
    private long resolveDateForEdit(String input, long previous) {
        if (input == null || input.trim().isEmpty()) {
            return previous;
        }
        long parsed = parseDateFlexible(input);
        if (parsed == 0L) {
            return previous;
        }
        return parsed;
    }

    /**
     * Guarda el viaje: insert si es nuevo, update si es edición.
     * Retorna el id del viaje (o filas afectadas en update); 0 si falla.
     */
    public long saveTrip(String title, String destination, String startDateStr, String endDateStr) {
        if (title == null || title.trim().isEmpty()) {
            return 0L;
        }
        if (currentTrip != null) {
            currentTrip.setTitle(title.trim());
            currentTrip.setDestination(destination != null ? destination.trim() : "");
            long newStart = resolveDateForEdit(startDateStr, currentTrip.getStartDate());
            long newEnd = resolveDateForEdit(endDateStr, currentTrip.getEndDate());
            currentTrip.setStartDate(newStart);
            currentTrip.setEndDate(newEnd);
            int rows = tripRepository.update(currentTrip);
            return rows > 0 ? currentTrip.getId() : 0L;
        } else {
            TripEntity trip = new TripEntity();
            trip.setUserId(userId);
            trip.setTitle(title.trim());
            trip.setDestination(destination != null ? destination.trim() : "");
            trip.setDescription("");
            trip.setStartDate(parseDateFlexible(startDateStr));
            trip.setEndDate(parseDateFlexible(endDateStr));
            return tripRepository.insert(trip);
        }
    }

    /** Indica si estamos editando un viaje existente. */
    public boolean isEditing() {
        return currentTrip != null;
    }
}
