package com.example.myeducationalapp;

import android.annotation.SuppressLint;
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


    private AVLTree<ComparablePair<Integer>> likedMessages = new AVLTree<>();

    /*
     * Gives all of the answers you've given for each question. Stored as
     * <question id, answer list>
     */
    private HashMap<String, List<String>> yourAnswers = new HashMap<>();

    private AVLTree<String> blockedUserList = new AVLTree<>();
    private AVLTree<String> successfullyAnsweredQuestions = new AVLTree<>();

    private int points = 0;

    int getPoints() {
        return points;
    }

    void logout() {
        points = 0;
        likedMessages = new AVLTree<>();
        yourAnswers = new HashMap<>();
        blockedUserList = new AVLTree<>();
        successfullyAnsweredQuestions = new AVLTree<>();
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

    private String escapeString(String str) {
        return str.replace("\\", "\\\\").replace("\n", "\\n").replace(";", "\\;");
    }

    private String unescapeString(String str) {
        return str.replace("\\;", ";").replace("\\n", "\n").replace("\\\\", "\\");
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        // FIXME: VERY UNTESTED

        String blockedUserStr = escapeString(blockedUserList.toString());
        String successfulAnswerStr = escapeString(successfullyAnsweredQuestions.toString());
        String likedMessageStr = escapeString(likedMessages.toString());

        StringBuilder hashMapEncoding = new StringBuilder();
        for (String key: yourAnswers.keySet()) {
            List<String> value = yourAnswers.get(key);
            hashMapEncoding.append(String.format(";%s;%d", key, value.size()));

            for (String innerValue: value) {
                hashMapEncoding.append(String.format(";%s", escapeString(innerValue)));
            }
        }

        return String.format("%s;%s;%s;%d", blockedUserStr, successfulAnswerStr, likedMessageStr, points) + hashMapEncoding;
    }


    void toggleLikeMessage(int threadID, int messageID) {
        var pair = new ComparablePair<>(threadID, messageID);
        if (likedMessages.search(pair) == null) {
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
