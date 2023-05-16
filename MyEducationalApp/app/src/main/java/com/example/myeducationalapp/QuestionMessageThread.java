package com.example.myeducationalapp;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;

/**
 * Contains all of the messages posted under a given question.
 *
 * @author u7468248 Alex Boxall
 */

public class QuestionMessageThread extends MessageThread {
    private String questionID;

    public QuestionMessageThread(String questionID) {
        this.questionID = questionID;
        downloadMessages();
    }

    @Override
    FirebaseResult downloadMessages() {
        return Firebase.getInstance().readQuestionComments(questionID).then((obj) -> {
            if (obj == null) {
                return null;
            }

            String[] allStrings = ((String) obj).split("\n");
            threadID = getThreadID();
            int index = 0;
            for (String message: allStrings) {
                messages.add(new Message(this, index, message));
                ++index;
            }
            return obj;
        });
    }

    @Override
    FirebaseResult uploadChanges() {
        return Firebase.getInstance().writeQuestionComments(questionID, this);
    }

}
