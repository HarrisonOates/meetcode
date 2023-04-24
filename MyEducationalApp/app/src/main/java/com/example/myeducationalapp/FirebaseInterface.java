package com.example.myeducationalapp;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * FirebaseInstance.java
 *
 * @author Alex Boxall
 */
public class FirebaseInterface {
    private static FirebaseInterface instance = null;

    public static FirebaseInterface getInstance() {
        if (instance == null) {
            instance = new FirebaseInterface();
        }
        return instance;
    }

    private final List<FirebaseObserver> observers = new ArrayList<>();

    /*private*/ public DatabaseReference database;
    public FirebaseDebugger debug;

    private FirebaseInterface() {
        database = FirebaseDatabase.getInstance("https://comp2100groupassignment-8427a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        debug = new FirebaseDebugger(database);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("FirebaseInterface", "GOT DATA");

                // notifyObservers(...);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FirebaseInterface", "CANCELLED");
            }
        };

        database.addValueEventListener(postListener);
    }

    public void attachObserver(FirebaseObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(/*...*/) {
        for (FirebaseObserver observer: observers) {
            observer.update(/*...*/);
        }
    }

    /**
     * Computes the location on the Firebase database where direct message history
     * between two people is held.
     *
     * @param username1 One of the two users. It doesn't matter which user is passed in first.
     * @param username2 The other user.
     * @return The location on the Firebase server where those two users' direct message history
     * is stored.
     */
    private String getDirectMessageStorageLocation(String username1, String username2) {
        if (username1.compareTo(username2) < 0) {
            return "dm_" + username1 + "_" + username2;
        } else {
            return "dm_" + username2 + "_" + username1;
        }
    }

    public Person readUserProfile(String username) {
        return null;
    }

    public MessageThread readDirectMessages(String username1, String username2) {
        String location = getDirectMessageStorageLocation(username1, username2);
        return null;
    }

    public Question readQuestion(String id) {
        return null;
    }

    public MessageThread readQuestionComments(String id) {
        return null;
    }

    public void writeUserProfile(Person person) {

    }

    public void writeDirectMessages(Person person1, Person person2, MessageThread messages) {
        String location = getDirectMessageStorageLocation(person1.getUsername(), person2.getUsername());

    }

    public void writeQuestionComments(Question question, MessageThread messages) {

    }

    public String readLoginDetails() {
        String loginString = "";
        return loginString;
    }

    public void writeLoginDetails(String userLoginString) {
        database.child("login").setValue(userLoginString);
    }

    public void savePerUserSettings(UserSettings settings) {

    }

    public UserSettings getPerUserSettings() {
        return null;
    }
}
