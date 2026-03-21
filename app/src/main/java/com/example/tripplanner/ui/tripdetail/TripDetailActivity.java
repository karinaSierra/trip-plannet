package com.example.tripplanner.ui.tripdetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripplanner.R;
import com.example.tripplanner.data.local.db.AppDatabase;
import com.example.tripplanner.data.local.entity.TripEntity;
import com.example.tripplanner.data.repository.TripRepository;
import com.example.tripplanner.ui.base.BaseActivity;
import com.example.tripplanner.ui.tripform.TripFormActivity;
import com.example.tripplanner.viewmodel.TripDetailViewModel;
import com.example.tripplanner.viewmodel.TripDetailViewModelFactory;
import com.google.android.material.button.MaterialButton;

/**
 * Pantalla que muestra el detalle de un viaje concreto.
 */
public class TripDetailActivity extends BaseActivity {

    public static final String EXTRA_TRIP_ID = "trip_id";

    private TripDetailViewModel viewModel;
    private TripEntity currentTrip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        long tripId = getIntent().getLongExtra(EXTRA_TRIP_ID, -1L);
        if (tripId < 0) {
            Toast.makeText(this, "Viaje no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);
        TripRepository tripRepository = new TripRepository(db);
        viewModel = new ViewModelProvider(this,
                new TripDetailViewModelFactory(tripRepository)).get(TripDetailViewModel.class);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDestination = findViewById(R.id.tvDestination);
        TextView tvStartDate = findViewById(R.id.tvStartDate);
        TextView tvEndDate = findViewById(R.id.tvEndDate);
        MaterialButton btnEdit = findViewById(R.id.btnEditTrip);
        MaterialButton btnDelete = findViewById(R.id.btnDeleteTrip);
        MaterialButton btnBack = findViewById(R.id.btnBack);

        currentTrip = viewModel.getTripById(tripId);
        if (currentTrip == null) {
            Toast.makeText(this, "Viaje no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle.setText(currentTrip.getTitle());
        tvDestination.setText(currentTrip.getDestination());
        tvStartDate.setText("Inicio: " + viewModel.formatDate(currentTrip.getStartDate()));
        tvEndDate.setText("Fin: " + viewModel.formatDate(currentTrip.getEndDate()));

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(TripDetailActivity.this, TripFormActivity.class);
            intent.putExtra(TripFormActivity.EXTRA_TRIP_ID, currentTrip.getId());
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> showDeleteConfirmation());
        btnBack.setOnClickListener(v -> finish());
    }

    private void showDeleteConfirmation() {
        if (currentTrip == null) return;
        new AlertDialog.Builder(this)
                .setTitle("Eliminar viaje")
                .setMessage("¿Estás seguro de eliminar este viaje?")
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    viewModel.deleteTrip(currentTrip);
                    Toast.makeText(TripDetailActivity.this, "Viaje eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .show();
    }
}


