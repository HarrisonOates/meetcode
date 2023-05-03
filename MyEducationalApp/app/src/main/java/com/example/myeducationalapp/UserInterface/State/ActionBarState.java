package com.example.myeducationalapp.UserInterface.State;

import com.example.myeducationalapp.UserInterface.UserInterfaceManager;

public abstract class ActionBarState {

    protected UserInterfaceManager actionBarManager;

    public ActionBarState(UserInterfaceManager actionBarManager) {
        this.actionBarManager = actionBarManager;
    }
}

