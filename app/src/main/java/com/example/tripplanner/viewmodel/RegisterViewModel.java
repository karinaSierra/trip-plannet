package com.example.tripplanner.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripplanner.data.local.entity.UserEntity;
import com.example.tripplanner.data.repository.UserRepository;
import com.example.tripplanner.security.PasswordHasher;
import com.example.tripplanner.viewmodel.base.BaseViewModel;

/**
 * ViewModel para el registro de usuarios.
 * Validaciones: nombre y email no vacíos, password mínimo 6 caracteres.
 */
public class RegisterViewModel extends BaseViewModel {

    private final UserRepository userRepository;

    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> registerError = new MutableLiveData<>();

    public RegisterViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }

    public LiveData<String> getRegisterError() {
        return registerError;
    }

    public void register(String name, String email, String password) {
        registerSuccess.setValue(false);
        registerError.setValue(null);

        if (name == null || name.trim().isEmpty()) {
            registerError.setValue("El nombre no puede estar vacío");
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            registerError.setValue("El email no puede estar vacío");
            return;
        }
        if (password == null || password.length() < 6) {
            registerError.setValue("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (userRepository.getUserByEmail(email.trim()) != null) {
            registerError.setValue("Ya existe un usuario con ese email");
            return;
        }

        UserEntity user = new UserEntity();
        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPasswordHash(PasswordHasher.hash(password));

        long id = userRepository.register(user);
        if (id > 0) {
            registerSuccess.setValue(true);
        } else {
            registerError.setValue("Error al registrar");
        }
    }
}
