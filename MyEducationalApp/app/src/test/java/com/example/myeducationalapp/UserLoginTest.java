package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserLoginTest {
    @Test
    public void standardLogin() {
        UserLogin login = UserLogin.getInstance();
        login.addUser("alex", "12345678");
        login.authoriseUser("alex", "12345678");

        String loggedInUser = login.getCurrentUsername();
        assertEquals(loggedInUser, "alex");
    }


}