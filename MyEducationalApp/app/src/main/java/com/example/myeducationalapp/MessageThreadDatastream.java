package com.example.myeducationalapp;

import com.example.myeducationalapp.Asynchronous;
import com.example.myeducationalapp.Firebase.FirebaseResult;
import com.example.myeducationalapp.Message;
import com.example.myeducationalapp.MessageThread;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class MessageThreadDatastream extends MessageThread {

    /**
     * Below this point is a number of functions that are required to simulate the datastream
     * for the assignment. These would be deleted before releasing for production.
     * These are NOT to be used outside of this very specific purpose.
     * Original code by Alex Boxall, and can be seen above.
     * @author Harrison Oates u7468212
     */

    abstract FirebaseResult downloadMessagesDatastream();
    abstract FirebaseResult uploadChangesDatastream();

    public void postMessageDatastream(String content, Message replyingTo, String sentBy) {
        int indexReplyingTo = replyingTo == null ? -1 : replyingTo.getIndex();

        // -1\t content here \t\t
        Message message = new Message(this, getMessages().size(), content, indexReplyingTo, sentBy);
        messages.add(message);

        message.runWhenReady((obj) -> {
            uploadChangesDatastream();
            return null;
        });
    }

    public void postMessageDatastream (String content, String sentBy) {
        postMessageDatastream(content, null, sentBy);
    }


}
