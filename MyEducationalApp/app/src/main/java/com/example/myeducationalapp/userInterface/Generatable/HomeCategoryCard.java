package com.example.myeducationalapp.userInterface.Generatable;

import android.graphics.Color;

import com.example.myeducationalapp.QuestionSet;
import com.example.myeducationalapp.R;

public class HomeCategoryCard {

    QuestionSet.Category category;

    public HomeCategoryCard(QuestionSet.Category category) {
        this.category = category;
    }

    public String getHeading() {
        return category.toString();
    }

    public String getSubheading() {

        StringBuilder subheading = new StringBuilder();
        int numberOfQuestions = QuestionSet.getInstance().getQuestionIDsInCategory(category).size();
        subheading.append(numberOfQuestions).append(" ");

        if (numberOfQuestions == 1) {
            subheading.append("Question");
        } else {
            subheading.append("Questions");
        }

        return subheading.toString();
    }

    public int getCardColor() {
        // TODO
        return Color.parseColor("#FFF25260");
    }

    public int getCardImage() {
        // TODO
        return R.drawable.shape_placeholder_square;
    }




}
