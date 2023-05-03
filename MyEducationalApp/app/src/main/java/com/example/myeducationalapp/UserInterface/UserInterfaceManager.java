package com.example.myeducationalapp.UserInterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserInterfaceManager {

    // Singleton instance
    private static UserInterfaceManager instance = null;
    // current state of the action bar
    ActionBarState actionBarState;

    // current visibility of the navigation menu
    boolean isNavigationMenuVisible;

    // current visibility of notification dot for messages on navigation menu
    Boolean isNavigationMenuNotificationVisible;

    DirectMessageNotificationMap directMessageNotification = new DirectMessageNotificationMap();

    // previous fragment required for backwards navigation
    // via the back button
    // TODO specify what Object is
    Object previousFragment;


    private UserInterfaceManager() {

        // ActionBarStarsState is default state as this class should be initialized
        // as soon as the app gets to the home screen
        this.actionBarState = new ActionBarStarsState(this);
    }

    // Access to object
    public static UserInterfaceManager getInstance() {
        if (instance == null) {
            instance = new UserInterfaceManager();
        }

        return instance;
    }

    public void updateState(Object currentlyDisplayedFragment) {
        // TODO
    }


    private void changeState() {

        // ActionBarStarsState -> ActionBarBackState
        // and
        // ActionBarBackState -> ActionBarStarsState
        if (actionBarState instanceof ActionBarStarsState) {
            this.actionBarState = new ActionBarBackState(this);
        } else if (actionBarState instanceof ActionBarBackState) {
            this.actionBarState = new ActionBarStarsState(this);
        } else {
            throw new IllegalStateException("ActionBarManager was found to contain an illegal state");
        }

    }

    public boolean isActionBarInStarState() {
        return actionBarState instanceof ActionBarStarsState;
    }

    public boolean isActionBarInBackState() {
        return actionBarState instanceof ActionBarBackState;
    }

    public Object getPreviousFragment() {
        return previousFragment;
    }

    public void setPreviousFragment(Object previousFragment) {
        this.previousFragment = previousFragment;
    }

    public void setNavigationMenuVisibility(boolean isNavigationMenuVisible) {
        this.isNavigationMenuVisible = isNavigationMenuVisible;
    }

    public boolean getNavigationMenuVisibility() {
        return isNavigationMenuVisible;
    }

    public void setNavigationMenuNotificationVisibility(boolean isNavigationMenuNotificationVisible) {
        this.isNavigationMenuNotificationVisible = isNavigationMenuNotificationVisible;
    }

    public boolean getNavigationMenuNotificationVisibility() {
        return isNavigationMenuNotificationVisible;
    }



}

class DirectMessageNotificationMap {

    private Map<String, Boolean> directMessageNotification;

    public DirectMessageNotificationMap() {
        directMessageNotification = new HashMap<>();
    }

    public Boolean put(String k, boolean v) {
        return directMessageNotification.put(k, v);
    }

    public Boolean remove(String k) {
        return directMessageNotification.remove(k);
    }



}
