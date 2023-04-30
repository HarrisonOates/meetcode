package com.example.myeducationalapp;

import com.example.myeducationalapp.Search.SearchToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class QuestionSet {
    public enum Category {Algorithm, ControlFlow, DataStructure, Miscellaneous, Recursion}

    // <Question ID, String[]{Question, answer} pair
    public HashMap<String, String[]> unusedQuestionSets;
    public HashMap<String, String[]> usedQuestionSets;
    public final char[] categoryRotation = new char[]{'0', '1', '2', '3', '4'};
    public int categoryIndex = 0;
    public HashMap<Category, Character> categoryCharacter;
    public QuestionSet() {
        unusedQuestionSets = new HashMap<>();
        usedQuestionSets = new HashMap<>();
        categoryCharacter = new HashMap<>();
        categoryCharacter.put(Category.Algorithm, '0');
        categoryCharacter.put(Category.ControlFlow, '1');
        categoryCharacter.put(Category.DataStructure, '2');
        categoryCharacter.put(Category.Miscellaneous, '3');
        categoryCharacter.put(Category.Recursion, '4');}
    public Set<String> futureQuestionIDs(){
        return unusedQuestionSets.keySet();
    }
    public Set<String> pastQuestionIDs(){
        return usedQuestionSets.keySet();
    }
    // Type 0: Algo, 1: ControlFlow, 2: DataStructure, 3: Misc, 4: Recursion | difficulty: 1 ~ 5
    // difficulty is in String so that category + difficulty does not give sum of ascii values of characters
    public void addQuestion(String question, String answer, Category category, String difficulty) {
        String uniqueID;
        // Make sure each ID is indeed unique
        do {
            uniqueID = categoryCharacter.get(category) + difficulty + UUID.randomUUID().toString();
        } while (unusedQuestionSets.containsKey(uniqueID));
        unusedQuestionSets.put(uniqueID, new String[]{question, answer});
    }

    /**
     * Randomly gets the question of the day where the category rotates every day.
     */
    public String[] getQuestionOfTheDay() {
        categoryIndex = (categoryIndex + 1) % 5;
        String uniqueID;
        do {
            List<String> keys = new ArrayList<>(unusedQuestionSets.keySet());
            Collections.shuffle(keys);
            uniqueID = keys.get(0);
        } while (uniqueID.charAt(0) != categoryRotation[categoryIndex]);
        String[] question = unusedQuestionSets.get(uniqueID);
        unusedQuestionSets.remove(uniqueID);
        usedQuestionSets.put(uniqueID, question);
        return question;

    }
    // I think we can just call one of these questions once a day?
    public void dataStructure1() {
        addQuestion(
                "What will be the output of map.get(2) after the execution of the codes below? \n"+
                        "A) 1, B) 2, C) 5, D) -1, E) 6 \n\n"+
                        "Map<Integer, String> map = new HashMap<Integer, String>(); \n"+
                        "map.put(1, \"2\"); \n"+
                        "map.put(2, \"5\"); \n"+
                        "map.put(2, \"-1\"); \n"+
                        "map.put(-1, \"6\");",
                "D",
                Category.DataStructure, "1"
        );
    }

    // TODO: add some questions and is this the structure of QuestionSet we want to use?
    public void dataStructure2() {
        // addQuestion();
    }

    public void dataStructure3() {
        // addQuestion();
    }

    public void dataStructure4() {
        // addQuestion();
    }

    public void dataStructure5() {
        // addQuestion();
    }
}
