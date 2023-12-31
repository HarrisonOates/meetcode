package com.example.myeducationalapp.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myeducationalapp.DirectMessageThread;
import com.example.myeducationalapp.MessageThread;
import com.example.myeducationalapp.User.UserLogin;
import com.example.myeducationalapp.User.UserLocalData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * Firebase.java
 *
 * A facade the rest of the program can use as an interface to the Firebase backend.
 * Abstracts away the low-level details of interacting with Firebase. Implements the
 * singleton, and facade design patterns.
 *
 * @author u7468248 Alex Boxall
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
     * any data has been modified on the Firebase backend.
     */
    private final List<FirebaseObserver> observers = new ArrayList<>();

    /**
     * A pointer to the internal Firebase object (i.e. the Android Firebase API).
     */
    private DatabaseReference database;

    /**
     * Private constructor that cannot usually be called (as this is a singleton). Should only
     * be called on the first call to getInstance().
     *
     * Retrieves a reference to the Firebase database using the Firebase API and installs any
     * relevant callbacks.
     */
    private Firebase() {
        database = FirebaseDatabase.getInstance("https://comp2100groupassignment-8427a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    }

    /**
     * Register a class as an observer of Firebase. The observer will be notified when any
     * changes are made to the Firebase database.
     * @param observer The observer object to add.
     */
    private void attachObserver(FirebaseObserver observer, List<String> path) {
        observer.path = path;
        observers.add(observer);
    }

    /**
     * Registers an observer for a given direct message thread between two users. Whenever anyone
     * reads or writes to this direct message thread (including sending, reading and liking), the
     * update() function of the observer will be called.
     *
     * @param observer The class whose update() method will be called when a change or read occurs
     * @param username1 One of the two users in the direct message thread. Order doesn't matter.
     * @param username2 One of the two users in the direct message thread. Order doesn't matter.
     */
    public void attachDirectMessageObserver(FirebaseObserver observer, String username1, String username2) {
        Log.d("attachDirectMessageObserver", username1 + " <-> " + username2);

        attachObserver(observer, getDirectMessageFilepath(username1, username2, true));
    }

    /**
     * Internally used to notify all of the observers that something has changed on the backend.
     * Should only be called when this happens.
     *
     * @param firebasePath The Firebase filepath that has been updated.
     */
    protected void notifyObservers(List<String> firebasePath) {
        for (FirebaseObserver observer: observers) {
            /*
             * Only call the observer if it is actually for them, and they aren't already in the
             * middle of an update() handler. This is critical as update() handlers may cause
             * other notifications to be sent out, and we would get infinite recursion otherwise.
             */
            if (observer.path.equals(firebasePath) && observer.isEnabled()) {
                observer.disable();
                observer.update();
            }
        }
    }

    /**
     * Given two usernames, determine the path on the Firebase database where those users'
     * direct message history is stored.
     *
     * @param username1 One of the two usernames (the order doesn't matter)
     * @param username2 One of the two usernames (the order doesn't matter)
     * @param escape Whether or not usernames with special characters should have their escaped
     *               values used in the result. Set to false only if the usernames have already
     *               been escaped.
     * @return A list containing the 'subdirectories' to enter on the Firebase database
     */
    List<String> getDirectMessageFilepath(String username1, String username2, boolean escape) {
        /*
         * We want to make sure the order is consistent, as it doesn't matter which username
         * gets passed in first.
         */
        if (username1.compareTo(username2) < 0) {
            if (escape) {
                return Arrays.asList("dm", FirebaseRequest.escapeUsername(username1), FirebaseRequest.escapeUsername(username2));
            } else {
                return Arrays.asList("dm", username1, username2);
            }
        } else {
            if (escape) {
                return Arrays.asList("dm", FirebaseRequest.escapeUsername(username2), FirebaseRequest.escapeUsername(username1));
            } else {
                return Arrays.asList("dm", username2, username1);
            }
        }
    }

    /**
     * Given a username, returns their user profile.
     *
     * @param username The username.
     * @return An asynchronous result, which when filled, will contain a string representing the
     * user's profile. This string would have been generated by Person.toString().
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
     * direct message history. This string would have been generated by MessageThread.toString()
     * or a subclass thereof.
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
        Log.d("readDirectMessages", username1 + " <-> " + username2);

        return FirebaseRequest.read(database, getDirectMessageFilepath(username1, username2, false));
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
     * Given a direct message conversation between two users, update it on the Firebase backend.
     *
     * @param username1 One of the two people. The order doesn't matter.
     * @param username2 One of the two people. The order doesn't matter.
     * @param messages The direct messages to be saved for these two users.
     * @return An asynchronous result that can be used to determine when the request has been
     * completed.
     * @throws AccessDeniedException Thrown if neither of the specified people are currently
     * logged in to this device.
     */
    public FirebaseResult writeDirectMessages(String username1, String username2, MessageThread messages) throws AccessDeniedException {
        /*
         * We can only write the direct message history if we are one of the two users participating
         * in the chat.
         */
        if (!UserLogin.getInstance().isUserLoggedIn(username1) &&
                !UserLogin.getInstance().isUserLoggedIn(username2)) {
            throw new AccessDeniedException("you do not have permission to read this conversation");
        }

        Log.d("writeDirectMessages", username1 + " <-> " + username2);

        return FirebaseRequest.write(database, getDirectMessageFilepath(username1, username2, false), messages.toString());
    }

    /**
     * Used to get all of the users that the currently logged in user has direct messaged
     * in the past. It takes in a callback, and for each user it finds that you've messaged,
     * it calls this callback with their direct message history. The DirectMessageThread is
     * guaranteed to have loaded at this point, and the most recent message is also
     * guaranteed to have loaded. All other messages are not guaranteed to have loaded.
     * The order the users are discovered and the callback called is undefined.
     *
     * @param callback A function to be called for each user that has been messaged.
     */
    public void getAllUsersYouHaveMessaged(Function<DirectMessageThread, Void> callback) {
        /*
         * Read the login file and wait for Firebase to load it.
         */
        readLoginDetails().then((obj) -> {
            String data = (String) obj;

            /*
             * Check if there are any users in the list.
             */
            if (!data.isEmpty()) {

                /*
                 * Iterate through the users.
                 */
                String[] userLoginInfos = data.split("\n");
                for (String userInfo : userLoginInfos) {
                    String username = userInfo.split(",")[0];

                    /*
                     * Load the direct message history between that user and this user and
                     * wait for it to load.
                     */
                    DirectMessageThread dms = new DirectMessageThread(username);
                    dms.runWhenReady((ignored) -> {
                        /*
                         * Check if there is any message history between the users.
                         */
                        if (!dms.getMessages().isEmpty()) {
                            /*
                             * Wait for the most recent message to load.
                             */
                            dms.getMessages().get(dms.getMessages().size() - 1).runWhenReady((ignored1) -> {
                                    /*
                                     * The direct message history and most recent message has loaded,
                                     * so we can now run the callback.
                                     */
                                    callback.apply(dms);
                                    return null;
                                }
                            );
                        }
                        return null;
                    });
                }
            }
            return null;
        });
    }

    /**
     * Synchronously returns a list of all of the usernames who have MeetCode accounts.
     * It is synchronous, but must be run in a separate thread than the main Firebase
     * thread.
     *
     * @return A list of the usernames on MeetCode.
     */
    public List<String> readAllUsernames() {
        /*
         * Get the content from Firebase and wait for completion.
         */
        String data = (String) readLoginDetails().await();

        List<String> usernames = new ArrayList<>();

        if (!data.isEmpty()) {
            /*
             * Iterate through each of the usernames.
             */
            String[] userLoginInfos = data.split("\n");
            for (String userInfo : userLoginInfos) {
                /*
                 * The password and salt is also stored, so just add the username to the result.
                 */
                usernames.add(userInfo.split(",")[0]);
            }
        }
        return usernames;
    }

    /**
     * Asynchronously returns a list of all of the usernames in the system.
     *
     * @return An asynchronous result, which when filled, can be cast to a List of Strings
     * containing the usernames
     */
    public FirebaseResult readAllUsernamesAsync() {
        /*
         * Get the content from Firebase and wait for completion.
         */
        return readLoginDetails().then((data_) -> {
            String data = (String) data_;
            List<String> usernames = new ArrayList<>();

            if (!data.isEmpty()) {
                /*
                 * Iterate through each of the usernames.
                 */
                String[] userLoginInfos = data.split("\n");
                for (String userInfo : userLoginInfos) {
                    /*
                     * The password and salt is also stored, so just add the username to the result.
                     */
                    usernames.add(userInfo.split(",")[0]);
                }
            }

            return usernames;
        });
    }

    /**
     * Returns all of the login details of all users on MeetCode.
     * @return An asynchronous result, which when filled, will contain a string representing the
     * all of the user login details. Each user will be on a separate line, and each line will
     * be in the format username,password,salt
     */
    public FirebaseResult readLoginDetails() {
        return FirebaseRequest.read(database, Arrays.asList("login"));
    }

    /**
     * Writes the login details for all users of MeetCode.
     * @param userLoginString A string containing all of the login details. Each user should be
     *                        on its own line, and the format of each line is username,password,salt
     * @return An asynchronous result that can be used to determine when the write has completed.
     */
    public FirebaseResult writeLoginDetails(String userLoginString) {
        return FirebaseRequest.write(database, Arrays.asList("login"), userLoginString);
    }

    /**
     * Writes the UserLocalData to Firebase. This consists of data specific to a given user that
     * is stored in the program memory for speed of access. This includes messages that have been
     * liked, the blocked user list and answers to questions.
     * @param username The user to write local data for
     * @param settings The local data to write. Generated from UserLocalData.toString()
     * @return An asynchronous result that can be used to determine when the write has completed.
     */
    public FirebaseResult writePerUserSettings(String username, UserLocalData settings) {
        return FirebaseRequest.write(database, Arrays.asList("per_user", username), settings.toString());
    }

    /**
     * Reads the UserLocalData from Firebase. This consists of data specific to a given user that
     * is stored in the program memory for speed of access. This includes messages that have been
     * liked, the blocked user list and answers to questions.
     * @param username The user to read local data for
     * @return An asynchronous result, which when filled, will contain a string representing the
     * user local data, which can be reloaded in UserLocalData.java
     */
    public FirebaseResult readPerUserSettings(String username) {
        return FirebaseRequest.read(database, Arrays.asList("per_user", username));
    }

    /**
     * Displays the value, and that of all children, of a given Firebase location.
     * Used for debugging purposes. Not guaranteed to print values in a sensible order
     * (i.e. folder hierarchies may not be printed in order).
     * @param root The Firebase object to recursively display information about.
     * @param pathSoFar The human readable path representing the object to recursively display
     *                  information from.
     */
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

    /**
     * Displays the entire contents of the Firebase database. Not guaranteed to print
     * values in a sensible order (i.e. folder hierarchies may not be printed in order).
     */
    public void dump() {
        Log.w("dump", "FIREBASE CONTENTS");
        dumpFrom(database, "");
    }

}
