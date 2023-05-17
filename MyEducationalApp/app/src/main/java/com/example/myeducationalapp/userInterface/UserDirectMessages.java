package com.example.myeducationalapp.userInterface;

import com.example.myeducationalapp.DirectMessageThread;
import com.example.myeducationalapp.userInterface.Generatable.MessageListCard;

import java.util.LinkedHashMap;

public class UserDirectMessages {

    private static UserDirectMessages instance = null;

    // This is a LinkedHashMap as order will be important in the order that the
    // DM's are rendered
    // Key = recipient's username, Value = recipient's MessageListCard
    public LinkedHashMap<String, MessageListCard> currentDirectMessages = new LinkedHashMap<>();

    public static UserDirectMessages getInstance() {
        if (instance == null) {
            instance = new UserDirectMessages();
        }
        return instance;
    }

    public boolean hasObserverBeenAttached(String recipientUsername) {
        return currentDirectMessages.get(recipientUsername).hasObserverBeenAttached;
    }

    public void setObserverHasBeenAttached(String recipientUsername, boolean hasObserverBeenAttached) {
        currentDirectMessages.get(recipientUsername).hasObserverBeenAttached = hasObserverBeenAttached;
    }

    public boolean isEmpty() {
        return currentDirectMessages.size() == 0;
    }

    public boolean doesUserExistInDirectMessages(String recipientUsername) {
        return currentDirectMessages.get(recipientUsername) != null;
    }

    public void logout() {
            currentDirectMessages = new LinkedHashMap<>();
    }


}
