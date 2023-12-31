package com.example.myeducationalapp;

import android.annotation.SuppressLint;

import com.example.myeducationalapp.User.Person;
import com.example.myeducationalapp.User.UserLocalData;
import com.example.myeducationalapp.User.UserLogin;

/**
 * Contains a message/comment, either a direct message or one that is underneath a question.
 *
 * @author u7468248 Alex Boxall
 */

public class Message extends Asynchronous {
    private String content;

    /**
     * The index number within the same message thread that this message is a reply to.
     * Set to -1 if this message is not replying to any other message.
     */
    private int replyingTo;

    /**
     * The person who sent this message.
     */
    private Person sentBy;

    /**
     * The chain of messages which this message is a part of.
     */
    protected MessageThread thread;

    /**
     * The index within the chain that this message is. The first message would be 0.
     */
    private int indexWithinThread;

    /**
     * A list of people who have liked the message. The values are loaded only as necessary
     * to reduce the amount of time spent waiting to load it from Firebase.
     */
    private AVLTree<String> likedBy;

    public int getIndex() {
        return indexWithinThread;
    }

    private void uploadNewLikedByCount() {
        thread.runAfterAllLoaded((obj) -> {
            thread.uploadChanges();
            return null;
        });
    }

    private String escapeString(String str) {
        return str.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t");
    }

    private String unescapeString(String str) {
        return str.replace("\\t", "\t").replace("\\n", "\n").replace("\\\\", "\\");
    }

    private void reloadLikedBy(String str) {
        likedBy = new AVLTree<>();

        if (str.length() != 0) {
            likedBy = likedBy.stringToTree(str, false);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("%d\t%s\t%s\t%s", replyingTo, escapeString(content), escapeString(sentBy.getUsername()), escapeString(likedBy.toString()));
    }

    /**
     * Creates a new message.
     *
     * @param parentThread
     * @param index
     * @param content
     * @param replyingTo
     */
    protected Message(MessageThread parentThread, int index, String content, int replyingTo) {
        this.thread = parentThread;
        this.likedBy = new AVLTree<>();
        this.indexWithinThread = index;
        this.replyingTo = replyingTo;
        this.content = content;
        this.sentBy = new Person(UserLogin.getInstance().getCurrentUsername());
        addWaitRequirement(sentBy.getWaitRequirement());
    }

    /**
     * Reloads an existing message.
     *
     * @param parentThread
     * @param index
     * @param data
     */
    protected Message(MessageThread parentThread, int index, String data) {
        this.thread = parentThread;
        this.likedBy = new AVLTree<>();
        this.indexWithinThread = index;

        /*
         * The -1 somehow makes it not remove 'empty' split cases (e.g. when a message
         * has no likes).
         *
         * https://stackoverflow.com/questions/14602062/java-string-split-removed-empty-values
         */
        String[] components = data.split("\t", -1);

        replyingTo = Integer.parseInt(components[0]);
        content = unescapeString(components[1]);
        sentBy = new Person(unescapeString(components[2]));
        reloadLikedBy(components[3]);

        addWaitRequirement(sentBy.getWaitRequirement());
    }

    public String getContent() {
        return content;
    }

    public int getLikeCount() {
        return likedBy.size();
    }

    public boolean isLikedByCurrentUser() {
        /*
         * For the responsiveness of the UI, it is important that this value is loaded quickly.
         * Hence we keep a second copy locally that can be accessed instantly.
         */
        return UserLocalData.getInstance().isMessageLiked(thread.getThreadID(), indexWithinThread);
    }

    public void toggleLikedByCurrentUser() {
        boolean wasLikedByCurrentUser = isLikedByCurrentUser();

        /*
         * For the responsiveness of the UI, it is important that this is done quickly.
         * Adjust the redundant copy of the variable we have.
         */
        UserLocalData.getInstance().toggleLikeMessage(thread.getThreadID(), indexWithinThread);


        /*
         * We must also ensure that it eventually reaches the server, so do this now.
         * This must first download the liked by information, in case someone else changed
         * it since we last read it. (If we didn't do another fetch here, someone else's likes
         * might disappear).
         */
        sentBy.runWhenReady((ignored) -> {
            String currentUser = UserLogin.getInstance().getCurrentUsername();

            if (wasLikedByCurrentUser) {
                likedBy.delete(currentUser);
            } else {
                likedBy.insert(currentUser);
            }

            /*
             * This may take time to complete, but we don't really care.
             */
            uploadNewLikedByCount();
            return null;
        });
    }

    public Person getPoster() {
        return sentBy;
    }

    /**
     * Below this point is a number of adjustments to functions that are required to simulate the datastream
     * for the assignment. These would be deleted before releasing for production.
     * These are NOT to be used outside of this very specific purpose.
     * @author Harrison Oates u7468212
     */

    /**
     * Creates a new message, specified by user.
     *
     * @param parentThread
     * @param index
     * @param content
     * @param replyingTo
     * @param sentBy - to change who appears to send the message
     *
     */
    protected Message(MessageThread parentThread, int index, String content, int replyingTo, String sentBy) {
        this.thread = parentThread;
        this.likedBy = new AVLTree<>();
        this.indexWithinThread = index;
        this.replyingTo = replyingTo;
        this.content = content;
        this.sentBy = new Person(sentBy);
        addWaitRequirement(this.sentBy.getWaitRequirement());
    }
}
