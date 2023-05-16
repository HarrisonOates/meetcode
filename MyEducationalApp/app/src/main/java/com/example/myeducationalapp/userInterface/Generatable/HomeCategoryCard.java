package com.example.myeducationalapp.userInterface.Generatable;

import android.graphics.Color;

import com.example.myeducationalapp.QuestionSet;
import com.example.myeducationalapp.R;

public class HomeCategoryCard {

    QuestionSet.Category category;

    public HomeCategoryCard(QuestionSet.Category category) {
        assert category != null;

        this.category = category;
    }

    public String getHeading() {
        return category.toString();
    }

    public String getSubheading() {

        StringBuilder subheading = new StringBuilder();
        int numberOfQuestions = QuestionSet.getInstance().getNumberOfQuestionsInCategory(category);
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
        return category.getPrimaryCategoryColor();
    }

    public int getSecondaryCardColor() {
        return category.getSecondaryCategoryColor();
    }

    public int getTertiaryCardColor() {
        return category.getTertiaryCategoryColor();
    }

    public int getCardImage() {
        // TODO
        return category.getCategoryImageDrawableID();
    }

    public QuestionSet.Category getCategory() {
        return category;
    }
}
