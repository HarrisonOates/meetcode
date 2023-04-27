package com.example.myeducationalapp;

import android.util.Log;

import com.example.myeducationalapp.Firebase.Firebase;

public class Person {
    public String getUsername() {
        return "";
    }

    @Override
    public String toString() {
        return "";
    }


    public Person(String username) {
        Firebase.getInstance().readUserProfile(username).then((obj) -> {
            String data = (String) obj;

            // TODO: do whatever is needed to load a Person object

            return null;
        });
    }
}
