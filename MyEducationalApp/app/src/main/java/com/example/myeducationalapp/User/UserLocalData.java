package com.example.myeducationalapp.User;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.myeducationalapp.AVLTree;
import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;
import com.example.myeducationalapp.Question.QuestionSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Stores all data that is local to a given user (e.g. posts they've liked, their answers to
 * questions, blocked user list, etc.).
 *
 * @author u7468248 Alex Boxall
 */
public class UserLocalData {

    /**
     * Implement the singleton design pattern.
     */
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
     * Stores all of the messages that this user has liked. If it is in the tree, it has
     * been liked.
     */
    AVLTree<ComparablePair<Integer>> likedMessages = new AVLTree<>();

    /**
     * Returns a list of the incorrect answers that this user has submitted.
     * @param questionID The question ID to get answers for
     * @return A list of all incorrect answers given so far
     */
    public List<String> getYourAnswers(String questionID) {
        return yourAnswers.get(questionID);
    }

    /**
     * Gives all of the answers you've given for each question. Stored as
     * <question id, answer list>
     */
    private HashMap<String, List<String>> yourAnswers = new HashMap<>();

    /**
     * Stores the usernames of all of the users that are blocked by the current user.
     */
    private AVLTree<String> blockedUserList = new AVLTree<>();

    /**
     * Stores the question IDs of all questions that this user has successfully answered.
     */
    private AVLTree<String> successfullyAnsweredQuestions = new AVLTree<>();

    private int points = 0;

    public int getPoints() {
        return points;
    }

    /**
     * Performs operations that need to occur every time the user logs out of the app.
     */
    public void logout() {
        points = 0;
        likedMessages = new AVLTree<>();
        yourAnswers = new HashMap<>();
        blockedUserList = new AVLTree<>();
        successfullyAnsweredQuestions = new AVLTree<>();
    }

    /**
     * Uploads the state of this user to Firebase, so it can be loaded later.
     */
    private void saveToDisk() {
        Firebase.getInstance().writePerUserSettings(UserLogin.getInstance().getCurrentUsername(), this);
    }

    /**
     * If this is set, loadFromDisk() will not do anything. This is used in unit tests.
     */
    public boolean noDiskReload = false;

    /**
     * Reload a user's local data from Firebase.
     * @return Null if no data was loaded, otherwise a FirebaseResult, that when filled, will
     * indicate that the operation is complete.
     */
    public FirebaseResult loadFromDisk() {
        /*
         * For certain unit tests we need to disable this function.
         */
        if (noDiskReload) {
            return null;
        }

        /*
         * Load the user settings from firebase and wait for the result.
         */
        return Firebase.getInstance().readPerUserSettings(UserLogin.getInstance().getCurrentUsername()).then((obj) -> {
            String data = (String) obj;

            if (data == null) {
                /*
                 * They are a new user, and have no local data created for them yet on Firebase.
                 * Therefore, we can just return and use the empty lists.
                 */
                return null;
            }

            /*
             * Separate the data into the separate objects they used to represent.
             */
            String[] parts = data.split(";", -1);
            String blockedUserStr = unescapeString(parts[0]);
            String successfulAnswer = unescapeString(parts[1]);
            String likedMessageStr = unescapeString(parts[2]);
            points = Integer.parseInt(parts[3]);

            /*
             * Reload the AVLTrees from those strings.
             */
            blockedUserList = new AVLTree<>();
            blockedUserList = blockedUserList.stringToTree(blockedUserStr, false);

            successfullyAnsweredQuestions = new AVLTree<>();
            successfullyAnsweredQuestions = successfullyAnsweredQuestions.stringToTree(successfulAnswer, false);

            likedMessages = new AVLTree<>();
            likedMessages = likedMessages.stringToTree(likedMessageStr, true);

            /*
             * Reload the hashmap by iterating through the string and putting together a new
             * hash map for it. The as the value is a list, we have key, followed by the list
             * length, and then that many value entries.
             */
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

    /**
     * The semicolon is used as a delimiter, so we need to escape it so the text can safely contain
     * a semicolon.
     * @param str A string which may have semicolons in it
     * @return An escaped string with no semicolons, such that the original string can be retrieved
     * by passing it to unescapeString
     */
    private String escapeString(String str) {
        return str.replace("\\", "\\\\").replace("\n", "\\n").replace(";", "\\a");
    }

    /**
     * Converts the result from escapeString back into its original form.
     * @param str A string returned from escapeString
     * @return The string that was passed into escapeString that gave the string inputted to this
     * function.
     */
    private String unescapeString(String str) {
        return str.replace("\\a", ";").replace("\\n", "\n").replace("\\\\", "\\");
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        /*
         * The trees can all be converted directly to a string, with their semicolons escaped.
         */
        String blockedUserStr = escapeString(blockedUserList.toString());
        String successfulAnswerStr = escapeString(successfullyAnsweredQuestions.toString());
        String likedMessageStr = escapeString(likedMessages.toString());

        /*
         * For the hashmap, we iterate over the entries, storing it as:
         * key, num_values, value 1, value 2, etc.,
         * as the value is a list of strings.
         */
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

    /**
     * Toggles whether or not the currently logged in user likes a certain message.
     * @param threadID The threadID of the message thread
     * @param messageID The message ID (index) within that thread
     */
    public void toggleLikeMessage(int threadID, int messageID) {
        var pair = new ComparablePair<>(threadID, messageID);
        if (likedMessages.search(pair) == null) {
            likedMessages.insert(pair);
        } else {
            likedMessages.delete(pair);
        }

        saveToDisk();
    }

    /**
     * Returns all of the incorrect answers that this user has submitted to a given question.
     * @param questionID The question ID
     * @return A list of strings with the incorrect answers
     */
    public List<String> getIncorrectAnswers(String questionID) {
        if (yourAnswers.containsKey(questionID)) {
            return yourAnswers.get(questionID);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Returns the number of questions in a category that have been successfully answered.
     * @param category The category to get the number of answered questions
     * @return The number of successfully answered questions
     */
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

    /**
     * Get the number of failed attempts at a given question.
     * @param questionID The question's ID
     * @return The number of failed attempts
     */
    public int getNumberOfFailedAttempts(String questionID) {
        return getIncorrectAnswers(questionID).size();
    }

    /**
     * Returns whether or not a question has been successfully answered
     * @param questionID The question's ID
     * @return True if the question has been successfully answered
     */
    public boolean hasQuestionBeenAnsweredCorrectly(String questionID) {
        return successfullyAnsweredQuestions.search(questionID) != null;
    }

    /**
     * Registers a correct answer for a given question.
     * @param questionID The question's ID
     */
    public void submitCorrectAnswer(String questionID) {
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

    /**
     * Registers an incorrect answer for a given question.
     * @param questionID The question's ID
     * @param answer The incorrect answer that the user typed in
     */
    public void submitIncorrectAnswer(String questionID, String answer) {
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

    /**
     * Toggles whether or not a user has been blocked by the current user.
     * @param username The user to block or unblock
     */
    public void toggleBlockUser(String username) {
        if (isUserBlocked(username)) {
            blockedUserList.delete(username);
        } else {
            blockedUserList.insert(username);
        }

        saveToDisk();
    }

    /**
     * Returns if a user has been blocked by the current user.
     * @param username The username
     * @return True if the user is blocked by the current user.
     */
    public boolean isUserBlocked(String username) {
        return blockedUserList.search(username) != null;
    }

    /**
     * Returns if a the current user has liked a given message.
     * @param threadID The thread ID of the message.
     * @param messageID The index within the thread of this message.
     * @return True if the message has been liked by the current user.
     */
    public boolean isMessageLiked(int threadID, int messageID) {
        ComparablePair<Integer> pair = new ComparablePair<>(threadID, messageID);
        return likedMessages.search(pair) != null;
    }

}
