package com.example.myeducationalapp.userInterface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myeducationalapp.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author u7300256 Nikhila Gurusinghe
 */
public class UserInterfaceManager {

    MutableLiveData<Boolean> isActionBarInBackState = new MutableLiveData<>();

    // current visibility of the navigation menu
    MutableLiveData<Boolean> isNavigationMenuVisible = new MutableLiveData<>();

    // current visibility of notification dot for messages on navigation menu
    MutableLiveData<Boolean> isNavMenuNotificationVisible = new MutableLiveData<>();

    MutableLiveData<String> toolbarTitle = new MutableLiveData<>();

    private final String homeToolbarTitle = "Home";


    // package-private constructor
    UserInterfaceManager() {
        // ActionBarStarsState is default state as this class should be initialized
        // as soon as the app gets to the home screen
        //this.actionBarState.setValue(new ActionBarStarsState(this));
        this.isActionBarInBackState.setValue(false);
        this.isNavigationMenuVisible.setValue(true);
        this.isNavMenuNotificationVisible.setValue(false);
    }

    public void setIsActionBarInBackState(boolean isActionBarInBackState) {
        this.isActionBarInBackState.setValue(isActionBarInBackState);
    }

    public LiveData<Boolean> getIsActionBarInBackState() {
        return isActionBarInBackState;
    }

    public void setNavigationMenuVisibility(boolean isNavigationMenuVisible) {
        this.isNavigationMenuVisible.setValue(isNavigationMenuVisible);
    }

    public LiveData<Boolean> getNavigationMenuVisibility() {
        return isNavigationMenuVisible;
    }

    public void setNavigationMenuNotificationVisibility(boolean isNavigationMenuNotificationVisible) {
        this.isNavMenuNotificationVisible.setValue(isNavigationMenuNotificationVisible);
    }

    public LiveData<Boolean> getNavMenuNotificationVisibility() {
        return isNavMenuNotificationVisible;
    }

    public LiveData<String> getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle.setValue(toolbarTitle);
    }

    public void enterNewFragment(String newToolbarTitle) {
        this.toolbarTitle.setValue(newToolbarTitle);

        this.isNavigationMenuVisible.setValue(true);

        if (Objects.equals(newToolbarTitle, homeToolbarTitle)) {
            this.isActionBarInBackState.setValue(false);
        } else {
            this.isActionBarInBackState.setValue(true);
        }
    }

    public void enterNewFragment(boolean isNavMenuVisible) {

        this.isNavigationMenuVisible.setValue(isNavMenuVisible);

        if (Objects.equals(this.toolbarTitle, homeToolbarTitle)) {
            this.isActionBarInBackState.setValue(false);
        } else {
            this.isActionBarInBackState.setValue(true);
        }
    }

    public void enterNewFragment(String newToolbarTitle, boolean isNavMenuVisible) {

        enterNewFragment(newToolbarTitle);

        this.isNavigationMenuVisible.setValue(isNavMenuVisible);

    }
}

