package com.example.myeducationalapp.userInterface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myeducationalapp.userInterface.Generatable.MessageListCard;
import com.example.myeducationalapp.userInterface.Generatable.QuestionCard;

import java.util.LinkedHashMap;

// https://developer.android.com/topic/libraries/architecture/viewmodel
public class UserInterfaceManagerViewModel extends ViewModel {

    private final MutableLiveData<UserInterfaceManager> uiState = new MutableLiveData<>(new UserInterfaceManager());

    private final MutableLiveData<LinkedHashMap<String, MessageListCard>> currentDirectMessages = new MutableLiveData<>(new LinkedHashMap<>());

    private final MutableLiveData<QuestionCard> currentlyDisplayedQuestion = new MutableLiveData<>(new QuestionCard());

    public LiveData<LinkedHashMap<String, MessageListCard>> getCurrentDirectMessages() {
        return currentDirectMessages;
    }

    public LiveData<UserInterfaceManager> getUiState() {
        return uiState;
    }

    public LiveData<QuestionCard> getCurrentlyDisplayedQuestion() {
        return currentlyDisplayedQuestion;
    }

}
