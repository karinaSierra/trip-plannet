package com.example.tripplanner.ui.triplist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.tripplanner.R;
import com.example.tripplanner.ui.base.BaseActivity;
import com.example.tripplanner.ui.login.LoginActivity;

/**
 * Pantalla que muestra la lista de viajes del usuario.
 * "Cerrar sesión" vuelve a LoginActivity.
 */
public class TripListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(TripListActivity.this, LoginActivity.class));
            finish();
        });
    }
}

