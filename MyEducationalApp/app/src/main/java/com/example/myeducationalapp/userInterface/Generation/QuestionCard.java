package com.example.myeducationalapp.userInterface.Generation;

import static com.example.myeducationalapp.QuestionSet.MAXIMUM_NUMBER_OF_MULTI_CHOICE_OPTIONS;

import android.text.Html;
import android.text.Spanned;

import com.example.myeducationalapp.Question;
import com.example.myeducationalapp.QuestionMessageThread;
import com.example.myeducationalapp.SyntaxHighlighting.DetectCodeBlock;

import java.util.ArrayList;
import java.util.List;

public class QuestionCard {

    public Question question;

    public QuestionMessageThread questionMessageThread;

    public String getHeading() {
        return question.getName();
    }

    public String getSubheading() {
        return question.getContent().split("```")[0];
    }

    public boolean doesQuestionHaveCodeBlock() {
        return question.getContent().contains("```");
    }

    public Spanned getCodeBlock() {
        // need to re-surround code block text in backticks
        return Html.fromHtml(DetectCodeBlock.parseCodeBlocks("```" + question.getContent().split("```")[1] + "```")) ;
    }

    public String getCategory() {
        return question.getCategory().name();
    }

    public boolean isQuestionMultiChoice() {
        return question.getContent().contains("A)");
    }

    /***
     * Gets all the multiple choice questions from the question contents.
     * @return will return a list of multiple choice options from order starting at A. The length
     *         of this list is not guaranteed to be anything and if isQuestionMultiChoice() is false
     *         then it will return a list of length 0
     */
    public List<String> getQuestionMultiChoiceOptions() {

        List<String> multiChoiceOptions = new ArrayList<>();
        String questionContent = question.getContent();

        // Looping through all questions
        for (int i = 'A'; i < 'A' + MAXIMUM_NUMBER_OF_MULTI_CHOICE_OPTIONS; i++) {

            // This will give us the index at the start of "<LETTER>)" so we need to go forward
            // two indices to get to the question content
            int indexOfQuestionLetter = questionContent.indexOf(String.valueOf((char) i) + ")");

            // if .indexOf returns -1 that means that we don't have anymore questions left to parse
            if (indexOfQuestionLetter == -1) {
                break;
            }

            // This will find the index of the next question start - 1;
            int indexOfEndOfQuestion = questionContent.indexOf(String.valueOf((char) (i + 1)) + ")") - 1;

            // if .indexOf returns -1 here that means we're at the end of the string
            // hence the end of the question will be the end of the string
            if (indexOfEndOfQuestion == -1) {
                indexOfEndOfQuestion = questionContent.length() - 1;
            }

            String multiChoiceContent = questionContent.substring(indexOfQuestionLetter + 2,
                    indexOfEndOfQuestion).trim();

            multiChoiceOptions.add(multiChoiceContent);
        }

        return multiChoiceOptions;
    }

    public String getAnswer() {
        return question.getAnswer();
    }


    public String getDifficulty() {

        // Difficulties are
        // 1. Beginner
        // 2. Intermediate
        // 3. Advanced
        // 4. Algorithm Aficionado
        // 5. Expert Debugger

        String difficulty = "Beginner";

        switch (question.getDifficulty()) {
            case 2:
                difficulty = "Intermediate";
                break;
            case 3:
                difficulty = "Advanced";
                break;
            case 4:
                difficulty = "Algorithm Aficionado";
                break;
            case 5:
                difficulty = "Expert Debugger";
                break;
        }

        return difficulty;
    }







}
