package com.example.myeducationalapp;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;

import java.nio.file.AccessDeniedException;

/**
 * Contains all of the messages sent between two users, and allows new messages to be added.
 *
 * @author u7468248 Alex Boxall
 */

public class DirectMessageThread extends MessageThread {
    private String withUsername;
    private Person withUser;

    public String getUsername() {
        return withUsername;
    }

    public DirectMessageThread(String withUsername) {
        this.withUsername = withUsername;
        this.withUser = new Person(withUsername);

        addWaitRequirement(downloadMessages());
    }

    public int getThreadID() {
        String username1 = UserLogin.getInstance().getCurrentUsername();
        String username2 = withUsername;

        if (username1.compareTo(username2) < 0) {
            return (username1 + username2).hashCode();
        } else {
            return (username2 + username1).hashCode();
        }
    }

    @Override
    FirebaseResult downloadMessages() {
        try {
            return Firebase.getInstance().readDirectMessages(UserLogin.getInstance().getCurrentUsername(), withUsername).then((obj) -> {
                String str = (String) obj;

                threadID = getThreadID();

                if (str != null && !str.isEmpty()) {
                    String[] allStrings = str.split("\n");
                    int index = 0;
                    for (String message: allStrings) {
                        messages.add(new Message(this, index, message));
                        ++index;
                    }
                }

                return obj;
            });

        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    FirebaseResult uploadChanges() {
        try {
            return Firebase.getInstance().writeDirectMessages(UserLogin.getInstance().getCurrentUsername(), withUsername, this);

        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

}
