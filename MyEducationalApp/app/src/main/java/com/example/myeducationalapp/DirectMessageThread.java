package com.example.myeducationalapp;

import android.util.Log;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;

import java.nio.file.AccessDeniedException;

public class DirectMessageThread extends MessageThread {
    private String withUsername;
    private Person withUser;

    public DirectMessageThread(String withUsername) {
        this.withUsername = withUsername;
        this.withUser = new Person(withUsername);

        addWaitRequirement(this.withUser.getWaitRequirement());
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

                /*
                 * This is slightly 'yikes' as it is very possible to have clashes, but
                 * let's not bother with that.
                 *
                 * TODO: fix this so it's just an incrementing value stored on the server
                 *       somewhere
                 */
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
