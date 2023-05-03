package com.example.myeducationalapp;

import android.util.Log;

class ComparablePair<T extends Comparable> implements Comparable<ComparablePair<T>> {
    public T first;
    public T second;

    ComparablePair(T a, T b) {
        this.first = a;
        this.second = b;
    }

    @Override
    public int compareTo(ComparablePair<T> t) {
        int res = first.compareTo(t.first);
        if (res == 0) {
            return second.compareTo(t.second);
        } else {
            return res;
        }
    }

    @Override
    public String toString() {
        return first.toString() + "@" + second.toString();
    }
}

public class UserSettings {

    static private UserSettings instance;

    private UserSettings() {

    }

    public static UserSettings getInstance() {
        if (instance == null) {
            instance = new UserSettings();
        }
        return instance;
    }

    /**
     * TODO: this needs to be saved to and from the disk - and on login and logout needs to be
     *       synced to the firebase server!!
     */
    AVLTree<ComparablePair<Integer>> likedMessages = new AVLTree<>();

    void toggleLikeMessage(int threadID, int messageID) {
        var pair = new ComparablePair<>(threadID, messageID);
        if (likedMessages.search(pair) == null) {
            Log.w("dbg", "about to insert a like, avl tree is: " + likedMessages.toString());
            likedMessages.insert(pair);
        } else {
            likedMessages.delete(pair);
        }
    }

    boolean isMessageLiked(int threadID, int messageID) {
        var pair = new ComparablePair<>(threadID, messageID);
        return likedMessages.search(pair) != null;
    }

    @Override
    public String toString() {
        return "";
    }
}
