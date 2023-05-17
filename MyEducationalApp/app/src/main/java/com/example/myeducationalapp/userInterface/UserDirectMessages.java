package com.example.myeducationalapp.userInterface;

import com.example.myeducationalapp.userInterface.Generatable.MessageListCard;

import java.util.LinkedHashMap;

public class UserDirectMessages {

    private static UserDirectMessages instance = null;

    // This is a LinkedHashMap as order will be important in the order that the
    // DM's are rendered
    private LinkedHashMap<String, MessageListCard> currentDirectMessages = new LinkedHashMap<>();

    public static UserDirectMessages getInstance() {
        if (instance == null) {
            instance = new UserDirectMessages();
            return instance;
        } else {
            return instance;
        }
    }

}
