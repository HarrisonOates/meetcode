package com.example.myeducationalapp;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

public class QuestionSetTest {
    @Test
    public void testQuestionsInMiscCategory() {
        QuestionSet qs = QuestionSet.getInstance();

        List<String> questions = qs.getQuestionIDsInCategory(QuestionSet.Category.Miscellaneous);
        assertEquals(questions.size(), 5);
        assertEquals(qs.getUsedQuestionSets().get(questions.get(2)).getAnswer(), "028064");
    }
}
