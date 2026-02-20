package com.example.tripplanner.ui.triplist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripplanner.R;
import com.example.tripplanner.data.local.db.AppDatabase;
import com.example.tripplanner.data.local.entity.TripEntity;
import com.example.tripplanner.data.repository.TripRepository;
import com.example.tripplanner.data.session.SessionManager;
import com.example.tripplanner.ui.base.BaseActivity;
import com.example.tripplanner.ui.login.LoginActivity;
import com.example.tripplanner.ui.tripform.TripFormActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.tripplanner.viewmodel.TripListViewModel;
import com.example.tripplanner.viewmodel.TripListViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Lista de viajes del usuario. RecyclerView + FAB. Recarga en onResume.
 */
public class TripListActivity extends BaseActivity {

    private TripListViewModel viewModel;
    private TripAdapter adapter;
    private SessionManager sessionManager;
    private RecyclerView recyclerTrips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();
        if (userId < 0) {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);
        TripRepository tripRepository = new TripRepository(db);
        viewModel = new ViewModelProvider(this,
                new TripListViewModelFactory(tripRepository)).get(TripListViewModel.class);

        recyclerTrips = findViewById(R.id.recyclerTrips);
        recyclerTrips.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripAdapter();
        adapter.setOnTripActionListener(this::onDeleteTrip);
        adapter.setOnTripClickListener(trip -> {
            Intent intent = new Intent(TripListActivity.this, TripFormActivity.class);
            intent.putExtra(TripFormActivity.EXTRA_TRIP_ID, trip.getId());
            startActivity(intent);
        });
        recyclerTrips.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabNewTrip);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(TripListActivity.this, TripFormActivity.class));
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            startActivity(new Intent(TripListActivity.this, LoginActivity.class));
            finish();
        });

        loadTrips();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTrips();
    }

    private void loadTrips() {
        int userId = sessionManager.getUserId();
        if (userId < 0) return;
        List<TripEntity> trips = viewModel.getTripsByUserId(userId);
        adapter.setTrips(trips);
    }

    private void onDeleteTrip(TripEntity trip) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar viaje")
                .setMessage("¿Estás segura de eliminar este viaje?")
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    viewModel.delete(trip);
                    loadTrips();
                    Toast.makeText(TripListActivity.this, "Viaje eliminado correctamente", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
