package com.example.tripplanner.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.example.tripplanner.R;
import com.example.tripplanner.data.session.SessionManager;
import com.example.tripplanner.ui.base.BaseActivity;
import com.example.tripplanner.ui.login.LoginActivity;

/**
 * Pantalla inicial: muestra el logo y redirige siempre a Login.
 */
public class SplashActivity extends BaseActivity {

    private static final long SPLASH_DELAY_MS = 2000L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(this::goNext, SPLASH_DELAY_MS);
    }

    private void goNext() {
        SessionManager sessionManager = new SessionManager(this);
        // Forzamos autenticación en cada apertura de app.
        sessionManager.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
