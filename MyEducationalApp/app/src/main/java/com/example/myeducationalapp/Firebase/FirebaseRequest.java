package com.example.myeducationalapp.Firebase;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Helper functions for reading and writing to Firebase. Abstracts away details such as special
 * characters, and the way object hierarchies are done.
 *
 * @author u7468248 Alex Boxall
 */

public class FirebaseRequest {

    /**
     * Firebase doesn't support having '.', '#', '$', '[', ']' in the path. Use this
     * to convert to a safe path.
     * @param username The username
     * @return The username, but without any special characters above
     */
    public static String escapeUsername(String username) {
        return username.replace("!", "!!")
                .replace(".", "!a")
                .replace("#", "!b")
                .replace("$", "!c")
                .replace("[", "!d")
                .replace("]", "!e");
    }

    /**
     * Given a path to an object on Firebase, return the Firebase object pointing to it.
     * @param current The root of the database
     * @param path The path to navigate to, with each fragment (folder) as a list entry in order
     * @return The reference to the object in the database
     */
    static DatabaseReference traversePath(DatabaseReference current, List<String> path) {
        for (int i = 0; i < path.size(); ++i) {
            String component = escapeUsername(path.get(i));
            current = current.child(component);
        }
        return current;
    }

    /**
     * Reads a file from Firebase.
     * @param root The root of the database
     * @param path The path to navigate to, with each fragment (folder) as a list entry in order
     * @return An asynchronous result, which when filled, contains the data read.
     */
    public static FirebaseResult read(DatabaseReference root, List<String> path) {
        return new FirebaseResult(traversePath(root, path));
    }

    /**
     * Reads a file from Firebase.
     * @param root The root of the database
     * @param path The path to navigate to, with each fragment (folder) as a list entry in order
     * @return An asynchronous result, which will be filled upon completion of the write.
     */
    public static FirebaseResult write(DatabaseReference root, List<String> path, Object value) {
        return new FirebaseResult(traversePath(root, path), value);
    }
}
