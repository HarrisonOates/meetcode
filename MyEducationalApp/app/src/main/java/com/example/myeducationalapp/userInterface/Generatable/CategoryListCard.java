package com.example.myeducationalapp.userInterface.Generatable;

import com.example.myeducationalapp.QuestionSet;
import com.example.myeducationalapp.UserLocalData;

public class CategoryListCard extends HomeCategoryCard {

    public CategoryListCard(QuestionSet.Category category) {
        super(category);
    }

    public String getStarProgress() {
        int numberOfQuestions = QuestionSet.getInstance().getNumberOfQuestionsInCategory(category);
        int numberOfAnsweredQuestions = UserLocalData.getInstance().getNumberOfAnsweredQuestionsInCategory(category);

        return "Achieved " + numberOfAnsweredQuestions + "/" + numberOfQuestions;
    }


}
