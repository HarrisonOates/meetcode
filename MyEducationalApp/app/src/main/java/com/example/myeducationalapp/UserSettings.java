package com.example.myeducationalapp;

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

    AVLTree<ComparablePair<Integer>> likedMessages = new AVLTree<>();

    void toggleLikeMessage(int threadID, int messageID) {
        var pair = new ComparablePair<>(threadID, messageID);
        if (likedMessages.search(pair) == null) {
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
