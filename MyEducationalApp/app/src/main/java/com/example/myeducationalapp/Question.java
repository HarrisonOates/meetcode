package com.example.myeducationalapp;

import com.example.myeducationalapp.Firebase.Firebase;

public class Question {
    String id;
    public Question(String questionID) {
        id = questionID;
        Firebase.getInstance().readQuestion(id).then((obj) -> {
            // TODO: do any loading needed

            return null;
        });
    }
    public String getID() {
        return id;
    }

    @Override
    public String toString() {
        return "";
    }
}
