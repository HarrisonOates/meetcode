package com.example.myeducationalapp;

import android.util.Log;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;

public class Person {
    private String username;

    public String getUsername() {
        return username;
    }

    public FirebaseResult _ready;

    @Override
    public String toString() {
        // TODO: do whatever is needed to save a Person object
        return username;
    }


    public Person(String username) {
        Log.w("dbg", "initing a person " + username + " with hashcode " + hashCode());
        _ready = Firebase.getInstance().readUserProfile(username).then((obj) -> {
            Log.w("dbg", "PERSON OBJECT READ " + username + " " + hashCode());

            String data = (String) obj;

            // TODO: do whatever is needed to load a Person object

            this.username = username;

            return obj;
        });
    }
}
