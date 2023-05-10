package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import android.util.Log;

import org.junit.Test;

/**
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserLoginTest {
    @Test
    public void standardLogin() {
        UserLogin login = UserLogin.getInstance();
        login.addUser("alex", "12345678");
        // Correct username and password pair should authorise the user
        login.authoriseUser("alex", "12345678");

        String loggedInUser = login.getCurrentUsername();
        assertEquals(loggedInUser, "alex");

        login.addUser("jayden", "12345678");
        login.addUser("nikhila", "12345678");

        // Having same password still result in different hashedPasswords because of the unique salts
        assertNotEquals(login.userLogins.get("alex"), login.userLogins.get("jayden"));
        assertNotEquals(login.userLogins.get("nikhila"), login.userLogins.get("jayden"));
        assertNotEquals(login.userLogins.get("alex"), login.userLogins.get("nikhila"));

        // Wrong password should not authorise the user
        login.addUser("geun", "123456789");
        assertFalse(login.authoriseUser("geun", "12345678"));

        System.out.println(login);
    }

    @Test (expected = Exception.class)
    public void weakPasswordTest() throws IllegalArgumentException {
        UserLogin login = UserLogin.getInstance();

        // Too weak password
        login.addUser("harry", "1234567");
    }

    @Test (expected = Exception.class)
    public void usernameAlreadyExistsTest() throws IllegalArgumentException {
        UserLogin login = UserLogin.getInstance();

        // The username already exists
        login.addUser("alex", "13578642");
    }
}