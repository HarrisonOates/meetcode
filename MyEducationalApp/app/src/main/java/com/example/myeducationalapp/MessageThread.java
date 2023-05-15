package com.example.myeducationalapp;

import com.example.myeducationalapp.Firebase.FirebaseResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A chain of messages, either direct messages or messages posted on a question.
 *
 * @author u7468248 Alex Boxall
 */

public abstract class MessageThread extends Asynchronous {
    // IMPORTANT: this class needs to have the most up-to-date version of the backend data at all
    //            times, as having stale data could lead to messages being lost.

    protected List<Message> messages = new ArrayList<>();
    protected int threadID;

    public int getThreadID() {
        return threadID;
    }

    // TODO: subclass based on either DM / questions
    abstract FirebaseResult downloadMessages();
    abstract FirebaseResult uploadChanges();

    FirebaseResult downloadMessageLikeCounts() {
        return downloadMessages();
    }

    protected MessageThread() {

    }

    void runAfterAllLoaded(Function<Object, Object> func) {
        clearRequirements();
        for (Message message: messages) {
            addWaitRequirement(message.getWaitRequirement());
        }
        runWhenReady(func);
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

    public List<Message> getMessages() {
        return messages;
    }

    public void postMessage(String content, Message replyingTo) {
        int indexReplyingTo = replyingTo == null ? -1 : replyingTo.getIndex();

        // -1\t content here \t\t
        Message message = new Message(this, getMessages().size(), content, indexReplyingTo);
        messages.add(message);

        message.runWhenReady((obj) -> {
            uploadChanges();
            return null;
        });
    }

    public void postMessage(String content) {
        postMessage(content, null);
    }
}
