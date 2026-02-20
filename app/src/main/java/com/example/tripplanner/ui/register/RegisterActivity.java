package com.example.tripplanner.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.tripplanner.R;
import com.example.tripplanner.ui.base.BaseActivity;
import com.example.tripplanner.ui.login.LoginActivity;

/**
 * Pantalla de registro de nuevos usuarios.
 * Botón "Volver al login" regresa a LoginActivity.
 */
public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnBack = findViewById(R.id.btnBackToLogin);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}

