package com.example.myeducationalapp.Firebase;

import java.util.List;

/**
 * An interface that can be extended to get notifications when data on Firebase changes.
 *
 * @author u7468248 Alex Boxall
 */

public abstract class FirebaseObserver {
    /**
     * The implementation of update() must call enable() after the final direct message read
     * from firebase, or upon returning, whichever is later.
     */
    public abstract void update();

    /*
     * Don't change this manually - Firebase.Firebase.java will set this for you.
     */
    List<String> path;

    private boolean enabled = true;

    boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    void disable() {
        enabled = false;
    }
}
