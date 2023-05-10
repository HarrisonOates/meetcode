package com.example.myeducationalapp.userInterface;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myeducationalapp.Person;
import com.example.myeducationalapp.R;
import com.example.myeducationalapp.userInterface.State.ActionBarBackState;
import com.example.myeducationalapp.userInterface.State.ActionBarStarsState;
import com.example.myeducationalapp.userInterface.State.ActionBarState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInterfaceManager {

    ActionBarState actionBarState;

    // current visibility of the navigation menu
    boolean isNavigationMenuVisible;

    // current visibility of notification dot for messages on navigation menu
    boolean isNavMenuNotificationVisible;

    // current visibility of notification dots for direct messages in the direct message fragment
    // TODO specify method/s to change and update this when new notifications occur/old ones go away
    DirectMessageNotificationMap directMessageNotification = new DirectMessageNotificationMap();

    // previous fragment required for backwards navigation
    // via the back button
    // TODO specify what Object is
    int previousFragmentAction;

    MutableLiveData<String> toolbarTitle = new MutableLiveData<>();


    // package-private constructor
    UserInterfaceManager() {
        // ActionBarStarsState is default state as this class should be initialized
        // as soon as the app gets to the home screen
        this.actionBarState = new ActionBarStarsState(this);
        this.isNavigationMenuVisible = true;
        this.isNavMenuNotificationVisible = false;
    }

    public void transitionState(int previousFragmentAction) {

        this.previousFragmentAction = previousFragmentAction;

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

    public int getIsActionBarInStarState() {
        if (actionBarState instanceof ActionBarStarsState) {
            return View.VISIBLE;
        } else if (actionBarState instanceof ActionBarBackState) {
            return View.GONE;
        } else {
            throw new
                    IllegalStateException("UserInterfaceManager was found to contain an illegal state");
        }
    }


    public int getIsActionBarInBackState() {
        if (actionBarState instanceof ActionBarStarsState) {
            return View.GONE;
        } else if (actionBarState instanceof ActionBarBackState) {
            return View.VISIBLE;
        } else {
            throw new
                    IllegalStateException("UserInterfaceManager was found to contain an illegal state");
        }
    }

    public void setNavigationMenuVisibility(boolean isNavigationMenuVisible) {
        this.isNavigationMenuVisible = isNavigationMenuVisible;
    }

    public boolean getNavigationMenuVisibility() {
        return isNavigationMenuVisible;
    }

    public void setNavigationMenuNotificationVisibility(boolean isNavigationMenuNotificationVisible) {
        this.isNavMenuNotificationVisible = isNavigationMenuNotificationVisible;
    }

    public int getNavMenuNotificationVisibility() {
        return isNavMenuNotificationVisible ? View.VISIBLE : View.GONE;
    }

    public LiveData<String> getToolbarTitle() {
        return toolbarTitle;
    }


    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle.setValue(toolbarTitle);
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

