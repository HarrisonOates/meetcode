package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class UserLocalDataTest {

    @Test
    public void testBlockingUsers() {
        UserLocalData localData = UserLocalData.getInstance();

        UserLogin login = UserLogin.getInstance();
        login.addUser("abc1234", "12345678");
        login.authoriseUser("abc1234", "12345678");

        assertFalse(localData.isUserBlocked("abc"));
        localData.toggleBlockUser("abc");
        assertTrue(localData.isUserBlocked("abc"));
        localData.toggleBlockUser("def");
        assertTrue(localData.isUserBlocked("abc"));
        assertTrue(localData.isUserBlocked("def"));
        localData.toggleBlockUser("abc");
        assertFalse(localData.isUserBlocked("abc"));
        assertTrue(localData.isUserBlocked("def"));

        login.logout();
    }

    @Test
    public void testSubmittingAnswers() {
        UserLocalData localData = UserLocalData.getInstance();

        UserLogin login = UserLogin.getInstance();
        login.addUser("abc1235", "12345678");
        login.authoriseUser("abc1235", "12345678");

        assertEquals(localData.getIncorrectAnswers("Q").size(), 0);
        assertFalse(localData.hasQuestionBeenAnsweredCorrectly("Q"));

        localData.submitIncorrectAnswer("Q", "A");
        assertEquals(localData.getIncorrectAnswers("Q").size(), 1);
        assertEquals(localData.getIncorrectAnswers("Q").get(0), "A");
        assertFalse(localData.hasQuestionBeenAnsweredCorrectly("Q"));
        assertEquals(localData.getNumberOfFailedAttempts("Q"), 1);

        localData.submitIncorrectAnswer("R", "B");
        assertEquals(localData.getIncorrectAnswers("Q").size(), 1);
        assertEquals(localData.getIncorrectAnswers("Q").get(0), "A");
        assertFalse(localData.hasQuestionBeenAnsweredCorrectly("Q"));
        assertEquals(localData.getNumberOfFailedAttempts("Q"), 1);

        localData.submitIncorrectAnswer("Q", "B");
        assertEquals(localData.getIncorrectAnswers("Q").size(), 2);
        assertEquals(localData.getNumberOfFailedAttempts("Q"), 2);
        assertFalse(localData.hasQuestionBeenAnsweredCorrectly("Q"));

        localData.submitCorrectAnswer("R");
        assertEquals(localData.getNumberOfFailedAttempts("Q"), 2);
        assertFalse(localData.hasQuestionBeenAnsweredCorrectly("Q"));
        assertTrue(localData.hasQuestionBeenAnsweredCorrectly("R"));
        assertEquals(localData.getNumberOfFailedAttempts("R"), 1);

        localData.submitCorrectAnswer("Q");
        assertEquals(localData.getIncorrectAnswers("Q").size(), 2);
        assertEquals(localData.getNumberOfFailedAttempts("Q"), 2);
        assertTrue(localData.hasQuestionBeenAnsweredCorrectly("Q"));

        login.logout();
    }

    @Test
    public void testPointsForFirstCorrect() {
        UserLocalData localData = UserLocalData.getInstance();

        UserLogin login = UserLogin.getInstance();
        login.addUser("abc1236", "12345678");
        login.authoriseUser("abc1236", "12345678");

        assertEquals(localData.getPoints(), 0);
        localData.submitCorrectAnswer("Q");
        assertEquals(localData.getPoints(), 3);

        login.logout();
    }

    @Test
    public void testPointsForSecondCorrect() {
        UserLocalData localData = UserLocalData.getInstance();

        UserLogin login = UserLogin.getInstance();
        login.addUser("abc1237", "12345678");
        login.authoriseUser("abc1237", "12345678");

        assertEquals(localData.getPoints(), 0);
        localData.submitIncorrectAnswer("Q", "A");
        assertEquals(localData.getPoints(), 0);
        localData.submitCorrectAnswer("Q");
        assertEquals(localData.getPoints(), 1);

        login.logout();
    }

    @Test
    public void testPointsForThirdCorrect() {
        UserLocalData localData = UserLocalData.getInstance();

        UserLogin login = UserLogin.getInstance();
        login.addUser("abc1238", "12345678");
        login.authoriseUser("abc1238", "12345678");

        assertEquals(localData.getPoints(), 0);
        localData.submitIncorrectAnswer("Q", "A");
        assertEquals(localData.getPoints(), 0);
        localData.submitIncorrectAnswer("Q", "A");
        assertEquals(localData.getPoints(), 0);
        localData.submitCorrectAnswer("Q");
        assertEquals(localData.getPoints(), 0);

        login.logout();
    }
}