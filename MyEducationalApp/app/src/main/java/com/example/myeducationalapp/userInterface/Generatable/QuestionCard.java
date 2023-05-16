package com.example.myeducationalapp.userInterface.Generatable;

import static com.example.myeducationalapp.QuestionSet.MAXIMUM_NUMBER_OF_MULTI_CHOICE_OPTIONS;

import android.text.Html;
import android.text.Spanned;

import com.example.myeducationalapp.Question;
import com.example.myeducationalapp.QuestionMessageThread;
import com.example.myeducationalapp.SyntaxHighlighting.DetectCodeBlock;

import java.util.ArrayList;
import java.util.List;

public class QuestionCard {

    private Question question;

    private QuestionMessageThread questionMessageThread;

    public List<String> multiChoiceOptions;

    public String getHeading() {
        return question.getName();
    }

    public String getSubheading() {
        return question.getContent().split("```")[0].replace("\n", "");
    }

    public boolean doesQuestionHaveCodeBlock() {
        return question.getContent().contains("```");
    }

    public void setQuestion(Question question) {
        this.question = question;
        if (isQuestionMultiChoice()) {
            multiChoiceOptions = getQuestionMultiChoiceOptions();
        }
        //questionMessageThread = new QuestionMessageThread(question.getID());
    }

    public String getQuestionID() {
        return question.getID();
    }

    public Spanned getCodeBlock() {
        // need to re-surround code block text in backticks
        return Html.fromHtml(DetectCodeBlock.parseCodeBlocks("```" + question.getContent().split("```")[1] + "```")) ;
    }

    public String getCategory() {

        // Replace all capital letters with " <capital letter>" then take the substring starting at
        // index 1 to avoid space added to start of category name
        return question.getCategory().name().replaceAll("([A-Z])", " $1").substring(1);
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
    private List<String> getQuestionMultiChoiceOptions() {

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
            int indexOfEndOfQuestion = questionContent.indexOf(String.valueOf((char) (i + 1)) + ")");

            // if .indexOf returns -1 here that means we're at the end of the string
            // hence the end of the question will be the end of the string
            if (indexOfEndOfQuestion == -1) {
                indexOfEndOfQuestion = questionContent.length();
            }

            String multiChoiceContent = questionContent.substring(indexOfQuestionLetter + 2,
                    indexOfEndOfQuestion).trim();

            multiChoiceOptions.add(multiChoiceContent);
        }

        return multiChoiceOptions;
    }

    public boolean isAnswerCorrect(String potentialAnswer) {
        String answer = question.getAnswer();

        // If it is multiple choice users will be pressing a button with corresponding TextView
        // such that TextView.getText() == potentialAnswer
        if (isQuestionMultiChoice()) {
            // if question is multiple choice then answer will be a single capital
            // character such as "E"
            char answerAsCharacter = answer.charAt(0);
            int indexInMultiChoiceOptionsList = answerAsCharacter - 'A';

            answer = multiChoiceOptions.get(indexInMultiChoiceOptionsList);

            return potentialAnswer.equals(answer);
        } else {
            // if question is not multiple choice then answer will be the actual answer we need
            // to compare against

            return potentialAnswer.equals(answer);
        }
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
