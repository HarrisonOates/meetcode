package com.example.myeducationalapp.User;

import androidx.annotation.Nullable;

import com.example.myeducationalapp.Asynchronous;
import com.example.myeducationalapp.Firebase.Firebase;
import java.util.Objects;

/**
 * Represents a user of the app.
 *
 * @author u7468248 Alex Boxall
 */

public class Person extends Asynchronous {
    private String username;

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return username;
    }


    public Person(String username) {
        this.username = username;

        addWaitRequirement(Firebase.getInstance().readUserProfile(username).then((obj) -> {
            /*
             * This handler is no longer required, we need to add a wait requirement so that
             * .runWhenReady() can be called on us.
             */
            return null;
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
