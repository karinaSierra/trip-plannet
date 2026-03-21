package com.example.tripplanner.ui.tripform;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tripplanner.R;
import com.example.tripplanner.data.local.db.AppDatabase;
import com.example.tripplanner.data.local.entity.TripEntity;
import com.example.tripplanner.data.repository.TripRepository;
import com.example.tripplanner.data.session.SessionManager;
import com.example.tripplanner.ui.base.BaseActivity;
import com.example.tripplanner.viewmodel.TripFormViewModel;
import com.example.tripplanner.viewmodel.TripFormViewModelFactory;

import androidx.lifecycle.ViewModelProvider;

/**
 * Formulario para crear o editar un viaje. Recibe trip_id por Intent para edición.
 */
public class TripFormActivity extends BaseActivity {

    public static final String EXTRA_TRIP_ID = "trip_id";

    private TripFormViewModel viewModel;
    private EditText etTitle, etDestination, etStartDate, etEndDate;
    private Button btnSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_form);

        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();
        if (userId < 0) {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);
        TripRepository tripRepository = new TripRepository(db);
        viewModel = new ViewModelProvider(this,
                new TripFormViewModelFactory(tripRepository, userId)).get(TripFormViewModel.class);

        etTitle = findViewById(R.id.etTitle);
        etDestination = findViewById(R.id.etDestination);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnSave = findViewById(R.id.btnSaveTrip);
        Button btnBack = findViewById(R.id.btnBack);

        long tripId = getIntent().getLongExtra(EXTRA_TRIP_ID, -1L);
        if (tripId >= 0) {
            TripEntity trip = viewModel.loadTrip(tripId);
            if (trip != null) {
                etTitle.setText(trip.getTitle());
                etDestination.setText(trip.getDestination() != null ? trip.getDestination() : "");
                etStartDate.setText(viewModel.formatDate(trip.getStartDate()));
                etEndDate.setText(viewModel.formatDate(trip.getEndDate()));
                btnSave.setText("Actualizar");
            }
        }

        btnSave.setOnClickListener(v -> saveTrip());
        btnBack.setOnClickListener(v -> finish());
    }

    private void saveTrip() {
        String title = etTitle.getText() != null ? etTitle.getText().toString() : "";
        String destination = etDestination.getText() != null ? etDestination.getText().toString() : "";
        String startDate = etStartDate.getText() != null ? etStartDate.getText().toString() : "";
        String endDate = etEndDate.getText() != null ? etEndDate.getText().toString() : "";

        long id = viewModel.saveTrip(title, destination, startDate, endDate);
        if (id > 0) {
            Toast.makeText(this,
                    viewModel.isEditing() ? "Viaje actualizado" : "Viaje guardado",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Título obligatorio", Toast.LENGTH_SHORT).show();
        }
    }
}
