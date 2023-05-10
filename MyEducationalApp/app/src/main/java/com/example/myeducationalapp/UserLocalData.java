package com.example.myeducationalapp;

import android.util.Log;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class UserLocalData {

    static private UserLocalData instance;

    private UserLocalData() {

    }

    public static UserLocalData getInstance() {
        if (instance == null) {
            instance = new UserLocalData();
        }
        return instance;
    }

    /**
     * TODO: this needs to be saved to and from the disk - and on login and logout needs to be
     *       synced to the firebase server!!
     */
    AVLTree<ComparablePair<Integer>> likedMessages = new AVLTree<>();

    /*
     * Gives all of the answers you've given for each question. Stored as
     * <question id, answer list>
     */
    HashMap<String, List<String>> yourAnswers = new HashMap<>();

    AVLTree<String> blockedUserList = new AVLTree<>();
    AVLTree<String> successfullyAnsweredQuestions = new AVLTree<>();

    private int points = 0;

    int getPoints() {
        return points;
    }

    private void saveToDisk() {
        Firebase.getInstance().writePerUserSettings(UserLogin.getInstance().getCurrentUsername(), this);
    }

    public FirebaseResult loadFromDisk() {
        return Firebase.getInstance().readPerUserSettings(UserLogin.getInstance().getCurrentUsername()).then((obj) -> {
            String data = (String) obj;

            // TODO: reverse the effects of toString()

            return null;
        });
    }


    @Override
    public String toString() {
        return "";
    }


    void toggleLikeMessage(int threadID, int messageID) {
        var pair = new ComparablePair<>(threadID, messageID);
        if (likedMessages.search(pair) == null) {
            Log.w("dbg", "about to insert a like, avl tree is: " + likedMessages.toString());
            likedMessages.insert(pair);
        } else {
            likedMessages.delete(pair);
        }

        saveToDisk();
    }

    List<String> getIncorrectAnswers(String questionID) {
        if (yourAnswers.containsKey(questionID)) {
            return yourAnswers.get(questionID);
        } else {
            return new ArrayList<>();
        }
    }

    int getNumberOfFailedAttempts(String questionID) {
        return getIncorrectAnswers(questionID).size();
    }

    boolean hasQuestionBeenAnsweredCorrectly(String questionID) {
        return successfullyAnsweredQuestions.search(questionID) != null;
    }

    void submitCorrectAnswer(String questionID) {
        if (!hasQuestionBeenAnsweredCorrectly(questionID)) {
            successfullyAnsweredQuestions.insert(questionID);

            int failedAttempts = getNumberOfFailedAttempts(questionID);
            if (failedAttempts == 0) {
                points += 3;
            } else if (failedAttempts == 1) {
                points += 1;
            }
        }

        saveToDisk();
    }

    void submitIncorrectAnswer(String questionID, String answer) {
        List<String> answers;

        if (yourAnswers.containsKey(questionID)) {
            answers = yourAnswers.get(questionID);
        } else {
            answers = new ArrayList<>();
        }

        answers.add(answer);
        yourAnswers.put(questionID, answers);

        saveToDisk();
    }

    void toggleBlockUser(String username) {
        if (isUserBlocked(username)) {
            blockedUserList.delete(username);
        } else {
            blockedUserList.insert(username);
        }

        saveToDisk();
    }

    boolean isUserBlocked(String username) {
        return blockedUserList.search(username) != null;
    }

    boolean isMessageLiked(int threadID, int messageID) {
        var pair = new ComparablePair<>(threadID, messageID);
        return likedMessages.search(pair) != null;
    }

}
