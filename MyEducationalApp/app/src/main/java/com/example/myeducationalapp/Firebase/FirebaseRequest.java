package com.example.myeducationalapp.Firebase;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class FirebaseRequest {
    static DatabaseReference traversePath(DatabaseReference current, List<String> path) {
        for (int i = 0; i < path.size(); ++i) {
            String component = path.get(i);
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
