package com.example.myeducationalapp.userInterface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myeducationalapp.userInterface.Generation.MessageListCard;

import java.util.LinkedHashMap;

// https://developer.android.com/topic/libraries/architecture/viewmodel
public class UserInterfaceManagerViewModel extends ViewModel {

    private final MutableLiveData<UserInterfaceManager> uiState = new MutableLiveData<>(new UserInterfaceManager());

    private final MutableLiveData<LinkedHashMap<String, MessageListCard>> currentDirectMessages = new MutableLiveData<>(new LinkedHashMap<>());

    public LiveData<LinkedHashMap<String, MessageListCard>> getCurrentDirectMessages() {
        return currentDirectMessages;
    }

    public LiveData<UserInterfaceManager> getUiState() {
        return uiState;
    }

}
