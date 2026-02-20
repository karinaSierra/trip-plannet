package com.example.tripplanner.ui.register;

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
import com.example.tripplanner.ui.base.BaseActivity;
import com.example.tripplanner.ui.login.LoginActivity;
import com.example.tripplanner.viewmodel.RegisterViewModel;
import com.example.tripplanner.viewmodel.RegisterViewModelFactory;

/**
 * Pantalla de registro. Validaciones y alta de usuario vía RegisterViewModel.
 */
public class RegisterActivity extends BaseActivity {

    private RegisterViewModel viewModel;
    private EditText etName, etEmail, etPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AppDatabase db = AppDatabase.getInstance(this);
        UserRepository userRepository = new UserRepository(db);
        viewModel = new ViewModelProvider(this, new RegisterViewModelFactory(userRepository))
                .get(RegisterViewModel.class);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnBack = findViewById(R.id.btnBackToLogin);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString() : "";
            String email = etEmail.getText() != null ? etEmail.getText().toString() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
            viewModel.register(name, email, password);
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        viewModel.getRegisterSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (Boolean.TRUE.equals(success)) {
                    Toast.makeText(RegisterActivity.this, "Registro correcto", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        viewModel.getRegisterError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null && !message.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
