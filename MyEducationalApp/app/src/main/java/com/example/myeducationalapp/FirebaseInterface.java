package com.example.myeducationalapp;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * FirebaseInterface.java
 **
 * A Singleton class used to perform read and write operations directly to the Firebase
 * database. This class does not perform any data validation or format conversion - this must be
 * performed in a higher-level class.
 *
 * TODO: should auth checks be done at this layer??
 *
 * For example, to write to a direct message conversation:
 *      FirebaseInstance fb = FirebaseInterface.getInstance();
 *      fb.writeFile(fb.getDMFilepath("PVC Pat", "Patricia PVC"), "content goes here...");
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

    private List<FirebaseObserver> observers = new ArrayList<>();

    private DatabaseReference database;
    private FirebaseInterface() {
        database = FirebaseDatabase.getInstance().getReference();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("FirebaseInterface", "GOT DATA");

                // notifyObservers();
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

    private void notifyObservers() {
        for (FirebaseObserver observer: observers) {
            observer.update();
        }
    }

    public Person readUserProfile(String username) {
        return null;
    }

    public MessageThread readDirectMessages(String username1, String username2) {
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

    }

    public void writeQuestionComments(Question question, MessageThread messages) {

    }

    public boolean authoriseUser(String username, String hashedPassword) {
        return false;
    }

    public void savePerUserSettings(UserSettings settings) {

    }

    public UserSettings getPerUserSettings() {
        return null;
    }
}
