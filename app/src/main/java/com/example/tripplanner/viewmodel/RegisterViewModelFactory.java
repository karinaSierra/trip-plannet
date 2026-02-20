package com.example.tripplanner.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripplanner.data.repository.UserRepository;

/**
 * Factory para crear RegisterViewModel con UserRepository.
 */
public class RegisterViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepository userRepository;

    public RegisterViewModelFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            @SuppressWarnings("unchecked")
            T result = (T) new RegisterViewModel(userRepository);
            return result;
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
