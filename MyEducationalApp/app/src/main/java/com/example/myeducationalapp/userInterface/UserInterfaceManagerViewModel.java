package com.example.myeducationalapp.userInterface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// https://developer.android.com/topic/libraries/architecture/viewmodel
public class UserInterfaceManagerViewModel extends ViewModel {

    private final MutableLiveData<UserInterfaceManager> uiState = new MutableLiveData<>(new UserInterfaceManager());

    public LiveData<UserInterfaceManager> getUiState() {
        return uiState;
    }

}
