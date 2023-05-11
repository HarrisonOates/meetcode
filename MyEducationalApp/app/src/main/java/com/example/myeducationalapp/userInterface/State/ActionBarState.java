package com.example.myeducationalapp.userInterface.State;

import com.example.myeducationalapp.userInterface.UserInterfaceManager;

public abstract class ActionBarState {

    protected UserInterfaceManager actionBarManager;

    public ActionBarState(UserInterfaceManager actionBarManager) {
        this.actionBarManager = actionBarManager;
    }
}

