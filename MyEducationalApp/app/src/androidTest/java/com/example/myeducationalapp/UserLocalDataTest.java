package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class UserLocalDataTest {
    @Test
    public void testToString() {
        UserLocalData localData = UserLocalData.getInstance();

        UserLogin login = UserLogin.getInstance();
        login.addUser("abc1231", "12345678");
        login.authoriseUser("abc1231", "12345678");

        assertEquals(";;;0", localData.toString());

        localData.toggleLikeMessage(123,456);
        assertEquals(";;123@456;0", localData.toString());

        localData.toggleLikeMessage(888,999);
        assertEquals(";;123@456,null,888@999;0", localData.toString());

        localData.toggleBlockUser("lol i'm blocked;;");
        assertEquals("lol i'm blocked\\;\\;;;123@456,null,888@999;0", localData.toString());

        localData.submitCorrectAnswer("Q1");
        assertEquals("lol i'm blocked\\;\\;;Q1;123@456,null,888@999;3", localData.toString());

        localData.submitIncorrectAnswer("Q2", "WRONG");
        assertEquals("lol i'm blocked\\;\\;;Q1;123@456,null,888@999;3;Q2;1;WRONG", localData.toString());

        localData.submitIncorrectAnswer("Q2", "WRONG AGAIN");
        assertEquals("lol i'm blocked\\;\\;;Q1;123@456,null,888@999;3;Q2;2;WRONG;WRONG AGAIN", localData.toString());

        localData.submitCorrectAnswer("Q2");
        assertEquals("lol i'm blocked\\;\\;;Q1,null,Q2;123@456,null,888@999;3;Q2;2;WRONG;WRONG AGAIN", localData.toString());

        localData.submitIncorrectAnswer("Q3", "WRONGO");
        assertEquals("lol i'm blocked\\;\\;;Q1,null,Q2;123@456,null,888@999;3;Q2;2;WRONG;WRONG AGAIN;Q3;1;WRONGO", localData.toString());

        login.logout();
    }

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
    public void testLikingMessages() {
        UserLocalData localData = UserLocalData.getInstance();

        UserLogin login = UserLogin.getInstance();
        login.addUser("abc12335", "12345678");
        login.authoriseUser("abc12335", "12345678");

        assertFalse(localData.isMessageLiked(1, 1));
        assertFalse(localData.isMessageLiked(1, 2));
        assertFalse(localData.isMessageLiked(2, 2));
        localData.toggleLikeMessage(1, 2);
        assertFalse(localData.isMessageLiked(1, 1));
        assertTrue(localData.isMessageLiked(1, 2));
        assertFalse(localData.isMessageLiked(2, 2));
        localData.toggleLikeMessage(2, 2);
        assertFalse(localData.isMessageLiked(1, 1));
        assertTrue(localData.isMessageLiked(1, 2));
        assertTrue(localData.isMessageLiked(2, 2));
        localData.toggleLikeMessage(2, 2);
        assertFalse(localData.isMessageLiked(1, 1));
        assertTrue(localData.isMessageLiked(1, 2));
        assertFalse(localData.isMessageLiked(2, 2));
        localData.toggleLikeMessage(1, 1);
        assertTrue(localData.isMessageLiked(1, 1));
        assertTrue(localData.isMessageLiked(1, 2));
        assertFalse(localData.isMessageLiked(2, 2));

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