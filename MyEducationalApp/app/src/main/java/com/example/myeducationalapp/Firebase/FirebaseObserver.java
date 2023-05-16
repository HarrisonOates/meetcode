package com.example.myeducationalapp.Firebase;

import java.util.List;

/**
 * An interface that can be extended to get notifications when data on Firebase changes.
 *
 * @author u7468248 Alex Boxall
 */

public abstract class FirebaseObserver {
    abstract void update();

    /*
     * Don't touch this - Firebase.Firebase.java will set this for you.
     */
    List<String> path;
}
