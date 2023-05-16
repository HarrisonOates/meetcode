package com.example.myeducationalapp.userInterface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myeducationalapp.Question;
import com.example.myeducationalapp.QuestionSet;
import com.example.myeducationalapp.userInterface.Generatable.CategoryListCard;
import com.example.myeducationalapp.userInterface.Generatable.HomeCategoryCard;
import com.example.myeducationalapp.userInterface.Generatable.MessageListCard;
import com.example.myeducationalapp.userInterface.Generatable.QuestionCard;

import java.util.LinkedHashMap;

/**
 * @author u7300256 Nikhila Gurusinghe
 */

// https://developer.android.com/topic/libraries/architecture/viewmodel
public class UserInterfaceManagerViewModel extends ViewModel {

    private final MutableLiveData<UserInterfaceManager> uiState = new MutableLiveData<>(new UserInterfaceManager());

    private final MutableLiveData<LinkedHashMap<String, MessageListCard>> currentDirectMessages = new MutableLiveData<>(new LinkedHashMap<>());

    private final MutableLiveData<QuestionCard> currentlyDisplayedQuestion = new MutableLiveData<>(new QuestionCard());

    private final MutableLiveData<CategoryListCard> currentlyDisplayedCategory = new MutableLiveData<>();

    public LiveData<LinkedHashMap<String, MessageListCard>> getCurrentDirectMessages() {
        return currentDirectMessages;
    }

    public LiveData<UserInterfaceManager> getUiState() {
        return uiState;
    }

    public LiveData<QuestionCard> getCurrentlyDisplayedQuestion() {
        return currentlyDisplayedQuestion;
    }

    public void setCurrentlyDisplayedQuestion(Question question) {
        if (question != null) {
            currentlyDisplayedQuestion.getValue().setQuestion(question);
        }
    }

    public LiveData<CategoryListCard> getCurrentlyDisplayedCategory() {
        return currentlyDisplayedCategory;
    }

    public void setCurrentlyDisplayedCategory(QuestionSet.Category category) {
        currentlyDisplayedCategory.setValue(new CategoryListCard(category));
    }

}
