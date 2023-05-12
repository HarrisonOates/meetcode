package com.example.myeducationalapp;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;
import com.example.myeducationalapp.userInterface.Generation.MessageListCard;

import java.util.Objects;

public class Person extends Asynchronous {
    private String username;

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        // TODO: do whatever is needed to save a Person object
        return username;
    }


    public Person(String username) {
        addWaitRequirement(Firebase.getInstance().readUserProfile(username).then((obj) -> {
            String data = (String) obj;

            // TODO: do whatever is needed to load a Person object

            this.username = username;

            return obj;
        }));
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        Person person = (Person) obj;

        return Objects.equals(this.username, person.username);
    }
}
