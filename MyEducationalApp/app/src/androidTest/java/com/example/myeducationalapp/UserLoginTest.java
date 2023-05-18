package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.myeducationalapp.User.UserLogin;

import org.junit.Test;

/**
 * @author u7469758 Geun Yun
 */

/**
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserLoginTest {
    @Test
    public void standardLogin() {
        UserLogin login = UserLogin.getInstance();
        login.addUser("alex2", "12345678");
        // Correct username and password pair should authorise the user
        login.authoriseUser("alex2", "12345678");

        assertTrue(login.isUserLoggedIn("alex2"));

        String loggedInUser = login.getCurrentUsername();
        assertEquals(loggedInUser, "alex2");

        login.addUser("jayden2", "12345678");
        login.addUser("nikhila2", "12345678");

        // Having same password still result in different hashedPasswords because of the unique salts
        assertNotEquals(login.userLogins.get("alex2"), login.userLogins.get("jayden2"));
        assertNotEquals(login.userLogins.get("nikhila2"), login.userLogins.get("jayden2"));
        assertNotEquals(login.userLogins.get("alex2"), login.userLogins.get("nikhila2"));

        // Wrong password should not authorise the user
        login.addUser("geun2", "123456789");
        assertFalse(login.authoriseUser("geun2", "12345678"));
    }

    @Test (expected = Exception.class)
    public void weakPasswordTest() throws IllegalArgumentException {
        UserLogin login = UserLogin.getInstance();

        // Too weak password
        login.addUser("harry2", "1234567");
    }

    @Test (expected = Exception.class)
    public void usernameAlreadyExistsTest() throws IllegalArgumentException {
        UserLogin login = UserLogin.getInstance();

        login.addUser("alex3", "124356354");

        // The username already exists
        login.addUser("alex3", "13578642");
    }
}