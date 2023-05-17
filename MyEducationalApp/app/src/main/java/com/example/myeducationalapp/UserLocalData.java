package com.example.myeducationalapp;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Used to store two integers in a way that implements the Comparable interface (so it can
 * be added to an AVL tree).
 *
 * @author u7468248 Alex Boxall
 */
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

/**
 * Stores all data that is local to a given user (e.g. posts they've liked, their answers to
 * questions, blocked user list, etc.).
 *
 * @author u7468248 Alex Boxall
 */
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


    AVLTree<ComparablePair<Integer>> likedMessages = new AVLTree<>();

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

    public boolean noDiskReload = false;
    public FirebaseResult loadFromDisk() {
        if (noDiskReload) {
            return null;
        }


        return Firebase.getInstance().readPerUserSettings(UserLogin.getInstance().getCurrentUsername()).then((obj) -> {
            String data = (String) obj;

            if (data == null) {
                // New user, so no data.
                return null;
            }

            String[] parts = data.split(";", -1);
            String blockedUserStr = unescapeString(parts[0]);
            String successfulAnswer = unescapeString(parts[1]);
            String likedMessageStr = unescapeString(parts[2]);
            points = Integer.parseInt(parts[3]);

            blockedUserList = new AVLTree<>();
            blockedUserList = blockedUserList.stringToTree(blockedUserStr, false);

            successfullyAnsweredQuestions = new AVLTree<>();
            successfullyAnsweredQuestions = successfullyAnsweredQuestions.stringToTree(successfulAnswer, false);

            likedMessages = new AVLTree<>();
            likedMessages = likedMessages.stringToTree(likedMessageStr, true);

            HashMap<String, List<String>> newMap = new HashMap<>();
            int i = 4;
            while (i < parts.length) {
                String key = parts[i++];
                int valueCount = Integer.parseInt(parts[i++]);
                List<String> values = new ArrayList<>();

                for (int j = 0; j < valueCount; ++j) {
                    values.add(parts[i++]);
                }

                newMap.put(key, values);
            }
            yourAnswers = newMap;
            return null;
        });
    }

    private String escapeString(String str) {
        return str.replace("\\", "\\\\").replace("\n", "\\n").replace(";", "\\a");
    }

    private String unescapeString(String str) {
        return str.replace("\\a", ";").replace("\\n", "\n").replace("\\\\", "\\");
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
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

    public int getNumberOfAnsweredQuestionsInCategory(QuestionSet.Category category) {
        List<String> questionsInCategory = QuestionSet.getInstance().getQuestionIDsInCategory(category);

        int numberOfAnsweredQuestionsInCategory = 0;

        for (String questionIDInCategory : questionsInCategory) {
            if (getInstance().hasQuestionBeenAnsweredCorrectly(questionIDInCategory)) {
                numberOfAnsweredQuestionsInCategory += 1;
            }
        }

        return numberOfAnsweredQuestionsInCategory;
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
                points += QuestionSet.getInstance().getUsedQuestionSets().get(questionID).getDifficulty();

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
        ComparablePair<Integer> pair = new ComparablePair<>(threadID, messageID);
        return likedMessages.search(pair) != null;
    }

}
