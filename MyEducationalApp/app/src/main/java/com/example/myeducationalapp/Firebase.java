package com.example.myeducationalapp;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FirebaseInstance.java
 *
 * @author Alex Boxall
 */
public class Firebase {
    private static Firebase instance = null;

    public static Firebase getInstance() {
        if (instance == null) {
            instance = new Firebase();
        }
        return instance;
    }

    private final List<FirebaseObserver> observers = new ArrayList<>();

    private DatabaseReference database;

    public DatabaseReference dbgGetRoot() {
        return database;
    }

    private Firebase() {
        database = FirebaseDatabase.getInstance("https://comp2100groupassignment-8427a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notifyObservers(/*...*/);
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

    private List<String> getDirectMessageFilepath(String username1, String username2) {
        if (username1.compareTo(username2) < 0) {
            return Arrays.asList("dm", username1, username2);
        } else {
            return Arrays.asList("dm", username2, username1);
        }
    }

    /*
     TODO: User authentication checks
     */

    public FirebaseResult readUserProfile(String username) {
        return FirebaseRequest.read(database, Arrays.asList("user", username));
    }

    public FirebaseResult readDirectMessages(String username1, String username2) {
        return FirebaseRequest.read(database, getDirectMessageFilepath(username1, username2));
    }

    public FirebaseResult readQuestion(String id) {
        return FirebaseRequest.read(database, Arrays.asList("q", id));
    }

    public FirebaseResult readQuestionComments(String id) {
        return FirebaseRequest.read(database, Arrays.asList("qc", id));
    }

    public FirebaseResult writeUserProfile(Person person) {
        return FirebaseRequest.write(database, Arrays.asList("user", person.getUsername()), person.toString());
    }

    public FirebaseResult writeDirectMessages(Person person1, Person person2, MessageThread messages) {
        return FirebaseRequest.write(database, getDirectMessageFilepath(person1.getUsername(), person2.getUsername()), messages.toString());
    }

    public FirebaseResult writeQuestionComments(Question question, MessageThread messages) {
        return FirebaseRequest.write(database, Arrays.asList("qc", question.getID()), messages.toString());
    }

    public FirebaseResult readLoginDetails() {
        return FirebaseRequest.read(database, Arrays.asList("login"));
    }

    public FirebaseResult writeLoginDetails(String userLoginString) {
        return FirebaseRequest.write(database, Arrays.asList("login"), userLoginString);
    }

    public FirebaseResult writePerUserSettings(String username, UserSettings settings) {
        return FirebaseRequest.write(database, Arrays.asList("per_user", username), settings.toString());
    }

    public FirebaseResult readPerUserSettings(String username) {
        return FirebaseRequest.read(database, Arrays.asList("per_user", username));
    }
}
