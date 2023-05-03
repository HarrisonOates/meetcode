package com.example.myeducationalapp;

public class ActionBarManager {

    // current state of the session
    ActionBarState actionBarState;

    // previous fragment required for backwards navigation
    // via the back button

    Object previousFragment;


    public ActionBarManager() {

        // ActionBarStarsState is default state as this class should be initialized
        // as soon as the app gets to the home screen
        this.actionBarState = new ActionBarStarsState(this);
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

}
