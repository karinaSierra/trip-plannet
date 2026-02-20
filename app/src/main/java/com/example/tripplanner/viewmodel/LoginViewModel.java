package com.example.tripplanner.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripplanner.data.local.entity.UserEntity;
import com.example.tripplanner.data.repository.UserRepository;
import com.example.tripplanner.data.session.SessionManager;
import com.example.tripplanner.viewmodel.base.BaseViewModel;

/**
 * ViewModel para el login.
 * Valida email/password, delega en UserRepository y guarda sesión con SessionManager.
 */
public class LoginViewModel extends BaseViewModel {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> loginError = new MutableLiveData<>();

    public LoginViewModel(UserRepository userRepository, SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }

    public void login(String email, String password) {
        loginSuccess.setValue(false);
        loginError.setValue(null);

        if (email == null || email.trim().isEmpty()) {
            loginError.setValue("El email no puede estar vacío");
            return;
        }
        if (password == null || password.length() < 6) {
            loginError.setValue("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        UserEntity user = userRepository.login(email.trim(), password);

        if (user != null) {
            sessionManager.saveLogin((int) user.getId(), user.getEmail());
            loginSuccess.setValue(true);
        } else {
            loginError.setValue("Email o contraseña incorrectos");
        }
    }
}
