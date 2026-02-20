package com.example.tripplanner.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.tripplanner.R;
import com.example.tripplanner.ui.base.BaseActivity;
import com.example.tripplanner.ui.register.RegisterActivity;
import com.example.tripplanner.ui.triplist.TripListActivity;

/**
 * Pantalla de login.
 * Navegación: Iniciar sesión → lista de viajes; Registrarse → pantalla de registro.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, TripListActivity.class);
            startActivity(intent);
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}

