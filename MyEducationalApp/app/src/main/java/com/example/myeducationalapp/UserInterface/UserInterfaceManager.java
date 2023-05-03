package com.example.myeducationalapp.UserInterface;

import com.example.myeducationalapp.Person;
import com.example.myeducationalapp.UserInterface.State.ActionBarBackState;
import com.example.myeducationalapp.UserInterface.State.ActionBarStarsState;
import com.example.myeducationalapp.UserInterface.State.ActionBarState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInterfaceManager {

    ActionBarState actionBarState;

    // current visibility of the navigation menu
    boolean isNavigationMenuVisible;

    // current visibility of notification dot for messages on navigation menu
    Boolean isNavigationMenuNotificationVisible;

    // current visibility of notification dots for direct messages in the direct message fragment
    // TODO specify method/s to change and update this when new notifications occur/old ones go away
    DirectMessageNotificationMap directMessageNotification = new DirectMessageNotificationMap();

    // previous fragment required for backwards navigation
    // via the back button
    // TODO specify what Object is
    Object previousFragment;


    // package-private constructor
    UserInterfaceManager() {

        // ActionBarStarsState is default state as this class should be initialized
        // as soon as the app gets to the home screen
        this.actionBarState = new ActionBarStarsState(this);
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
            throw new
                    IllegalStateException("UserInterfaceManager was found to contain an illegal state");
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

