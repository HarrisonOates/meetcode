package com.example.myeducationalapp.Firebase;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class FirebaseRequest {

    /**
     * Firebase doesn't support having '.', '#', '$', '[', ']' in the path. Use this
     * to convert to a safe path.
     * @param username The username
     * @return The username, but without any special characters above
     */
    private static String escapeUsername(String username) {
        return username.replace("!", "!!")
                .replace(".", "!.")
                .replace("#", "!#")
                .replace("$", "!$")
                .replace("[", "![")
                .replace("]", "!]");
    }

    static DatabaseReference traversePath(DatabaseReference current, List<String> path) {
        for (int i = 0; i < path.size(); ++i) {
            String component = escapeUsername(path.get(i));
            current = current.child(component);
        }
        return current;
    }

    public static FirebaseResult read(DatabaseReference root, List<String> path) {
        return new FirebaseResult(traversePath(root, path));
    }

    public static FirebaseResult write(DatabaseReference root, List<String> path, Object value) {
        return new FirebaseResult(traversePath(root, path), value);
    }
}
