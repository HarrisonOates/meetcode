package com.example.myeducationalapp;

import android.util.Log;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;

import java.util.Arrays;
import java.util.List;

public class QuestionMessageThread extends MessageThread {
    private String questionID;
    private Question question;

    public QuestionMessageThread(String questionID) {
        this.questionID = questionID;
        this.question = new Question(questionID);
        downloadMessages();
    }

    @Override
    FirebaseResult downloadMessages() {
        return Firebase.getInstance().readQuestionComments(questionID).then((obj) -> {
            List<String> allStrings = Arrays.asList(((String) obj).split("\n"));
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
        return Firebase.getInstance().writeQuestionComments(question, this);
    }

}
