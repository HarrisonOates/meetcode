package com.example.myeducationalapp;

import com.example.myeducationalapp.DirectMessageThread;
import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;
import com.example.myeducationalapp.Message;
import com.example.myeducationalapp.Person;
import com.example.myeducationalapp.UserLogin;

import java.nio.file.AccessDeniedException;

/**
 * Below this point is a number of functions that are required to simulate the datastream
 * for the assignment. These would be deleted before releasing for production.
 * These are NOT to be used outside of this very specific purpose.
 * @author Harrison Oates u7468212
 * @author Alex Boxall - Original class code
 */

public class DirectMessageThreadDatastream extends MessageThreadDatastream {

    private String withUsername;
    private Person withUser;

    private String originatingUsername;
    private Person originatingUser;

    public String getUsername() {
        return withUsername;
    }

    public DirectMessageThreadDatastream(String withUsername, String originatingUsername) {
        this.withUsername = withUsername;
        this.withUser = new Person(withUsername);
        this.originatingUsername = originatingUsername;
        this.originatingUser = new Person(originatingUsername);

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

    FirebaseResult downloadMessages(){
        return downloadMessagesDatastream();
    }
    @Override
    FirebaseResult downloadMessagesDatastream() {
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
                    for (String message : allStrings) {
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

    FirebaseResult uploadChanges(){
        return uploadChangesDatastream();
    }
    @Override
    FirebaseResult uploadChangesDatastream() {
        return Firebase.getInstance().writeDirectMessagesDatastream(UserLogin.getInstance().getCurrentUsername(), withUsername, this);
    }
}