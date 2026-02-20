package com.example.tripplanner.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripplanner.data.repository.UserRepository;
import com.example.tripplanner.data.session.SessionManager;

/**
 * Factory para crear LoginViewModel con UserRepository y SessionManager.
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    public LoginViewModelFactory(UserRepository userRepository, SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            @SuppressWarnings("unchecked")
            T result = (T) new LoginViewModel(userRepository, sessionManager);
            return result;
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
