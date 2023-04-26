package com.example.myeducationalapp;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

public class DirectMessageThread extends MessageThread {
    private String withUsername;
    private Person withUser;

    public DirectMessageThread(String withUsername) {
        this.withUsername = withUsername;
        this.withUser = new Person(withUsername);
        downloadMessages();
    }

    @Override
    FirebaseResult downloadMessages() {
        try {
            return Firebase.getInstance().readDirectMessages(UserLogin.getInstance().getCurrentUsername(), withUsername).then((obj) -> {
                List<String> allStrings = Arrays.asList(((String) obj).split("\n"));
                threadID = Integer.parseInt(allStrings.remove(0));
                int index = 0;
                for (String message: allStrings) {
                    messages.add(new Message(this, index, message));
                    ++index;
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
