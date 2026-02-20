package com.example.tripplanner.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripplanner.viewmodel.base.BaseViewModel;

/**
 * ViewModel para la pantalla de login.
 */
public class LoginViewModel extends BaseViewModel {

    private final MutableLiveData<Boolean> loginInProgress = new MutableLiveData<>(false);

    public LiveData<Boolean> getLoginInProgress() {
        return loginInProgress;
    }

    // TODO: añadir métodos para manejar el proceso de login.
}

