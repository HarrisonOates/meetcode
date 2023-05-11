package com.example.myeducationalapp.userInterface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myeducationalapp.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserInterfaceManager {

    //MutableLiveData<ActionBarState> actionBarState = new MutableLiveData<>();
    MutableLiveData<Boolean> isActionBarInBackState = new MutableLiveData<>();

    // current visibility of the navigation menu
    MutableLiveData<Boolean> isNavigationMenuVisible = new MutableLiveData<>();

    // current visibility of notification dot for messages on navigation menu
    MutableLiveData<Boolean> isNavMenuNotificationVisible = new MutableLiveData<>();

    // current visibility of notification dots for direct messages in the direct message fragment
    // TODO specify method/s to change and update this when new notifications occur/old ones go away
    DirectMessageNotificationMap directMessageNotification = new DirectMessageNotificationMap();

    // previous fragment required for backwards navigation
    // via the back button
    // TODO specify what Object is
    int previousFragmentAction;

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

//    public void transitionState(int previousFragmentAction) {
//
//        this.previousFragmentAction = previousFragmentAction;
//
//        // ActionBarStarsState -> ActionBarBackState
//        // and
//        // ActionBarBackState -> ActionBarStarsState
//        if (actionBarState.getValue() instanceof ActionBarStarsState) {
//            this.actionBarState.setValue(new ActionBarBackState(this));
//        } else if (actionBarState.getValue() instanceof ActionBarBackState) {
//            this.actionBarState.setValue(new ActionBarStarsState(this));
//        } else {
//            throw new
//                    IllegalStateException("UserInterfaceManager was found to contain an illegal state");
//        }
//    }
//
//    public int getIsActionBarInStarState() {
//        if (actionBarState.getValue() instanceof ActionBarStarsState) {
//            return View.VISIBLE;
//        } else if (actionBarState.getValue() instanceof ActionBarBackState) {
//            return View.GONE;
//        } else {
//            throw new
//                    IllegalStateException("UserInterfaceManager was found to contain an illegal state");
//        }
//    }
//
//
//    public int getIsActionBarInBackState() {
//        if (actionBarState.getValue() instanceof ActionBarStarsState) {
//            return View.GONE;
//        } else if (actionBarState.getValue() instanceof ActionBarBackState) {
//            return View.VISIBLE;
//        } else {
//            throw new
//                    IllegalStateException("UserInterfaceManager was found to contain an illegal state");
//        }
//    }

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

class DirectMessageNotificationMap {

    private Map<Person, Boolean> directMessageNotification;

    public DirectMessageNotificationMap() {
        directMessageNotification = new HashMap<>();
    }

    public Boolean put(Person k, boolean v) {
        if (k != null) {
            return directMessageNotification.put(k, v);
        }

        return false;
    }

    public Boolean remove(Person k) {
        if (k != null) {
            return directMessageNotification.remove(k);
        }

        return false;
    }

    public ArrayList<Person> getAllNotifications() {

        ArrayList<Person> allNotifications = new ArrayList<>();

        directMessageNotification.forEach((person, hasNotification) -> {

            if (hasNotification) {
                allNotifications.add(person);
            }

        });

        return allNotifications;
    }
}

