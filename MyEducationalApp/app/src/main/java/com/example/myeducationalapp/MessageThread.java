package com.example.myeducationalapp;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;

import java.util.Arrays;
import java.util.List;

/**
 * MessageThread.java
 *
 * A chain of messages, either direct messages or messages posted on a question.
 */

public abstract class MessageThread {
    // IMPORTANT: this class needs to have the most up-to-date version of the backend data at all
    //            times, as having stale data could lead to messages being lost.

    List<Message> messages;
    int threadID;

    // TODO: subclass based on either DM / questions
    abstract FirebaseResult downloadMessages();
    abstract FirebaseResult uploadChanges();


    protected MessageThread() {

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Message message: messages) {
            result.append(message.toString());
            result.append("\n");
        }

        return result.toString();
    }
}
