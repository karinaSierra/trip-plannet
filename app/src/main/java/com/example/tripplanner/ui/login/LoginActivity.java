package com.example.tripplanner.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripplanner.R;
import com.example.tripplanner.data.local.db.AppDatabase;
import com.example.tripplanner.data.repository.UserRepository;
import com.example.tripplanner.data.session.SessionManager;
import com.example.tripplanner.ui.base.BaseActivity;
import com.example.tripplanner.ui.register.RegisterActivity;
import com.example.tripplanner.ui.triplist.TripListActivity;
import com.example.tripplanner.viewmodel.LoginViewModel;
import com.example.tripplanner.viewmodel.LoginViewModelFactory;

/**
 * Pantalla de login. Valida, guarda sesión y navega a TripList si es correcto.
 */
public class LoginActivity extends BaseActivity {

    private LoginViewModel viewModel;
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppDatabase db = AppDatabase.getInstance(this);
        UserRepository userRepository = new UserRepository(db);
        SessionManager sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this,
                new LoginViewModelFactory(userRepository, sessionManager))
                .get(LoginViewModel.class);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText() != null ? etEmail.getText().toString() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
            viewModel.login(email, password);
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        viewModel.getLoginSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (Boolean.TRUE.equals(success)) {
                    startActivity(new Intent(LoginActivity.this, TripListActivity.class));
                    finish();
                }
            }
        });

        viewModel.getLoginError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null && !message.isEmpty()) {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
