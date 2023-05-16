package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author u7468248 Alex Boxall
 */

public class DirectMessageTest {
    @Test
    public void testReadingDirectMessage() {
        /*
         * These test cannot use the callbacks we normally use (otherwise the
         * test will end before the callback happens) - so we must use pauses,
         * and run it in another thread.
         */
        UserLogin login = UserLogin.getInstance();
        login.performUnitTestLogin();
        assertEquals("alex", login.getCurrentUsername());
        DirectMessageThread dms = new DirectMessageThread("geun");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(17, dms.getMessages().size());
        assertEquals("testing!", dms.getMessages().get(0).getContent());
        assertEquals("This is definitely geun", dms.getMessages().get(7).getContent());
        assertEquals("alex", dms.getMessages().get(0).getPoster().getUsername());
        assertEquals("geun", dms.getMessages().get(7).getPoster().getUsername());
        assertTrue(dms.getMessages().get(0).isLikedByCurrentUser());
        assertFalse(dms.getMessages().get(1).isLikedByCurrentUser());
        assertFalse(dms.getMessages().get(2).isLikedByCurrentUser());
        assertTrue(dms.getMessages().get(3).isLikedByCurrentUser());
        assertTrue(dms.getMessages().get(4).isLikedByCurrentUser());
        assertTrue(dms.getMessages().get(5).isLikedByCurrentUser());
        assertFalse(dms.getMessages().get(6).isLikedByCurrentUser());
    }
}
