package com.example.myeducationalapp;

import com.example.myeducationalapp.Search.SearchToken;

import java.util.HashMap;
import java.util.UUID;

public class QuestionSet {
    public enum Category {Algorithm, ControlFlow, DataStructure, Miscellaneous, Recursion}
    // <Question ID, String[]{Question, answer} pair
    public HashMap<String, String[]> questionSets;

    // Type 0: Algo, 1: ControlFlow, 2: DataStructure, 3: Misc, 4: Recursion | difficulty: 1 ~ 5
    public QuestionSet(String question, String answer, Category category, int difficulty) {
        String uniqueID = "";
        HashMap<Category, Character> categoryMap = new HashMap<>();
        categoryMap.put(Category.Algorithm, '0');
        categoryMap.put(Category.ControlFlow, '1');
        categoryMap.put(Category.DataStructure, '2');
        categoryMap.put(Category.Miscellaneous, '3');
        categoryMap.put(Category.Recursion, '4');
        // Make sure each ID is indeed unique
        do {
            uniqueID = categoryMap.get(category) + difficulty + UUID.randomUUID().toString();
        } while (!questionSets.containsKey(uniqueID));
        questionSets.put(uniqueID, new String[]{question, answer});
    }

    public QuestionSet dataStructure1 = new QuestionSet(
            "What will be the output of map.get(2) after the execution of the codes below? \n" +
                    "A) 1, B) 2, C) 5, D) -1, E) 6 \n\n" +
                    "Map<Integer, String> map = new HashMap<Integer, String>(); \n" +
                    "map.put(1, \"2\"); \n" +
                    "map.put(2, \"5\"); \n" +
                    "map.put(2, \"-1\"); \n" +
                    "map.put(-1, \"6\");",
            "D",
            Category.DataStructure, 1
    );

    public QuestionSet getDataStructure2 = null;
    public QuestionSet getDataStructure3 = null;
    public QuestionSet getDataStructure4 = null;
    public QuestionSet getDataStructure5 = null;
}
