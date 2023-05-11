package com.example.myeducationalapp.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myeducationalapp.MessageThread;
import com.example.myeducationalapp.Person;
import com.example.myeducationalapp.Question;
import com.example.myeducationalapp.UserLogin;
import com.example.myeducationalapp.UserLocalData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * FirebaseInstance.java
 *
 * A class the rest of the program can use as an interface to the Firebase backend.
 * Abstracts away the low-level details of interacting with Firebase.
 *
 * Implements the singleton design pattern.
 *
 * @author Alex Boxall
 */
public class Firebase {
    /**
     * Hold the single instance of the Firebase singleton.
     */
    private static Firebase instance = null;

    /**
     * Gets the singleton object.
     * @return A pointer to the Firebase singleton.
     */
    public static Firebase getInstance() {
        if (instance == null) {
            instance = new Firebase();
        }
        return instance;
    }

    /**
     * All observers in this array will have their .notify() method called whenever
     * new relevant data has been modified on the Firebase backend.
     */
    private final List<FirebaseObserver> observers = new ArrayList<>();

    /**
     * A pointer to the internal Firebase object (i.e. the Android Firebase API).
     */
    private DatabaseReference database;

    /**
     * For debugging purposes only, exposes the 'database' pointer.
     * @return The internal 'database' pointer
     */
    public DatabaseReference dbgGetRoot() {
        return database;
    }

    /**
     * Private constructor that cannot usually be called (as this is a singleton). Should only
     * be called on the first call to getInstance().
     *
     * Retrieves a reference to the Firebase database using the Firebase API and installs any
     * relevant callbacks.
     */
    private Firebase() {
        database = FirebaseDatabase.getInstance("https://comp2100groupassignment-8427a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                 * Tell all the observers about the data change.
                 * TODO: work out what to tell them, and when (i.e. only notify if needed)
                 */
                notifyObservers(/*...*/);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*
                 * We're not planning to cancel requests (that is known of), so we would not
                 * expect it to be anything but an error if this happened.
                 */
                Log.e("FirebaseInterface", "CANCELLED");
            }
        };

        database.addValueEventListener(postListener);
    }

    /**
     * Register a class as an observer of Firebase. The observer will be notified when any
     * relevant changes are made to the Firebase database.
     * @param observer The observer object to add.
     */
    public void attachObserver(FirebaseObserver observer) {
        observers.add(observer);
    }

    /**
     * Internally used to notify all of the observers that something has changed on the backend.
     * Should only be called when this happens.
     */
    private void notifyObservers(/*...*/) {
        for (FirebaseObserver observer: observers) {
            observer.update(/*...*/);
        }
    }

    /**
     * Given two usernames, determine the path on the Firebase database where those users'
     * direct message history would be stored.
     *
     * @param username1 One of the two usernames (the order doesn't matter)
     * @param username2 One of the two usernames (the order doesn't matter)
     * @return A list containing the 'subdirectories' to enter on the Firebase database
     */
    private List<String> getDirectMessageFilepath(String username1, String username2) {
        /*
         * We want to make sure the order is consistent, as it doesn't matter which username
         * gets passed in first.
         */
        if (username1.compareTo(username2) < 0) {
            return Arrays.asList("dm", username1, username2);
        } else {
            return Arrays.asList("dm", username2, username1);
        }
    }

    /**
     * Given a username, returns their user profile.
     *
     * @param username The username.
     * @return An asynchronous result, which when filled, will contain a string representing the
     * user's profile. This string would have been generated by Person.toString.
     */
    public FirebaseResult readUserProfile(String username)  {
        return FirebaseRequest.read(database, Arrays.asList("user", username));
    }

    /**
     * Given the usernames of two users, returns their direct message history.
     *
     * @param username1 One of the two usernames (order doesn't matter)
     * @param username2 One of the two usernames (order doesn't matter)
     * @return An asynchronous result, which when filled, will contain a string representing the
     * direct message history. This string would have been generated by MessageThread.toString
     * or a subset thereof.
     * @throws AccessDeniedException Thrown if neither of the requested users are the logged into
     * this device.
     */
    public FirebaseResult readDirectMessages(String username1, String username2) throws AccessDeniedException {
        /*
         * We can only read the direct message history if we are one of the two users participating
         * in the chat.
         */
        if (!UserLogin.getInstance().isUserLoggedIn(username1) && !UserLogin.getInstance().isUserLoggedIn(username2)) {
            throw new AccessDeniedException("you do not have permission to read this conversation");
        }
        return FirebaseRequest.read(database, getDirectMessageFilepath(username1, username2));
    }

    /**
     * Returns all of the questions currently available.
     *
     * @return An asynchronous result, which when filled, will contain a comma-separated string
     * containing all of the possible question IDs.
     */
    public FirebaseResult readAllQuestions() {
        return FirebaseRequest.read(database, Arrays.asList("qs"));
    }

    /**
     * Given a question ID found through a call to readAllQuestions(), returns the details
     * about that question. The message thread about this question is not returned here.
     *
     * @param id The question ID
     * @return An asynchronous result, which when filled, will contain a string representing the
     * question. This string would have been generated by Question.toString
     */
    public FirebaseResult readQuestion(String id) {
        return FirebaseRequest.read(database, Arrays.asList("q", id));
    }

    /**
     * Given a question ID found through a call to readAllQuestions(), returns the message
     * history for that question.
     *
     * @param id The question ID
     * @return An asynchronous result, which when filled, will contain a string representing the
     * question's message history. This string would have been generated by MessageThread.toString
     * or a subclass thereof.
     */
    public FirebaseResult readQuestionComments(String id) {
        return FirebaseRequest.read(database, Arrays.asList("qc", id));
    }

    /**
     * Given a user profile, update it on the Firebase backend.
     *
     * @param person The user profile to update on the backend.
     * @return An asynchronous result that can be used to determine when the request has been
     * completed.
     * @throws AccessDeniedException Thrown if the user that is being updated is not currently
     * logged in to this device.
     */
    public FirebaseResult writeUserProfile(Person person) throws AccessDeniedException {
        if (!UserLogin.getInstance().isUserLoggedIn(person.getUsername())) {
            throw new AccessDeniedException("cannot write to another user's profile");
        }
        return FirebaseRequest.write(database, Arrays.asList("user", person.getUsername()), person.toString());
    }

    /**
     * Given a direct message conversation between two users, update it on the Firebase backend.
     *
     * @param person1 One of the two people. The order doesn't matter.
     * @param person2 One of the two people. The order doesn't matter.
     * @param messages The direct messages to be saved for these two users.
     * @return An asynchronous result that can be used to determine when the request has been
     * completed.
     * @throws AccessDeniedException Thrown if neither of the specified people are currently
     * logged in to this device.
     */
    public FirebaseResult writeDirectMessages(String username1, String username2, MessageThread messages) throws AccessDeniedException {
        // TODO: RACE CONDITIONS
        //       if both users are trying to write to the database at the same time (i.e. they both
        //       post at the same time).

        /*
         * We can only write the direct message history if we are one of the two users participating
         * in the chat.
         */
        if (!UserLogin.getInstance().isUserLoggedIn(username1) &&
                !UserLogin.getInstance().isUserLoggedIn(username2)) {
            throw new AccessDeniedException("you do not have permission to read this conversation");
        }

        return FirebaseRequest.write(database, getDirectMessageFilepath(username1, username2), messages.toString());
    }

    public List<String> readAllUsernames() {
        String data = (String) readLoginDetails().await();

        List<String> usernames = new ArrayList<>();

        if (!data.isEmpty()) {
            String[] userLoginInfos = data.split("\n");
            for (String userInfo : userLoginInfos) {
                usernames.add(userInfo.split(",")[0]);
            }
        }

        return usernames;
    }

    public FirebaseResult debugDeleteAllDirectMessages(String username1, String username2) {
        return FirebaseRequest.write(database, getDirectMessageFilepath(username1, username2), "");
    }

    public FirebaseResult writeQuestionComments(Question question, MessageThread messages) {
        // TODO: RACE CONDITIONS

        return FirebaseRequest.write(database, Arrays.asList("qc", question.getID()), messages.toString());
    }

    public FirebaseResult readLoginDetails() {
        return FirebaseRequest.read(database, Arrays.asList("login"));
    }

    public FirebaseResult writeLoginDetails(String userLoginString) {
        return FirebaseRequest.write(database, Arrays.asList("login"), userLoginString);
    }

    public FirebaseResult writePerUserSettings(String username, UserLocalData settings) {
        return FirebaseRequest.write(database, Arrays.asList("per_user", username), settings.toString());
    }

    public FirebaseResult readPerUserSettings(String username) {
        return FirebaseRequest.read(database, Arrays.asList("per_user", username));
    }

    public void dumpFrom(DatabaseReference root, String pathSoFar) {
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (var ignored: snapshot.getChildren()) {
                    ++i;
                }

                Log.w("dump", pathSoFar + (i == 0 ? ": " + snapshot.getValue() : ""));

                for (var child: snapshot.getChildren()) {
                    dumpFrom(child.getRef(), pathSoFar + "/" + child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        root.get();
    }

    public void eraseAllData(String safetyCheck) {
        if (!safetyCheck.equals("yes, I actually want to delete all data from firebase")) {
            throw new RuntimeException("if you really want to delete everything, look at the source of Firebase.eraseAllData");
        }

        database.removeValue();
    }

    public void dump() {
        Log.w("dump", "FIREBASE CONTENTS");
        dumpFrom(database, "");
    }
}
