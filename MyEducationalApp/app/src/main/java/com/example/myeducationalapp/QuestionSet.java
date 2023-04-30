package com.example.myeducationalapp;

import com.example.myeducationalapp.Search.SearchToken;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class QuestionSet {
    public enum Category {Algorithm, ControlFlow, DataStructure, Miscellaneous, Recursion}

    // <Question ID, String[]{Question, answer} pair
    public HashMap<String, String[]> questionSets;
    public HashMap<Category, Character> categoryCharacter;
    public QuestionSet() {
        questionSets = new HashMap<>();
        categoryCharacter = new HashMap<>();
        categoryCharacter.put(Category.Algorithm, '0');
        categoryCharacter.put(Category.ControlFlow, '1');
        categoryCharacter.put(Category.DataStructure, '2');
        categoryCharacter.put(Category.Miscellaneous, '3');
        categoryCharacter.put(Category.Recursion, '4');}
    public Set<String> getQuestionIDs(){
        return questionSets.keySet();
    }

    // Type 0: Algo, 1: ControlFlow, 2: DataStructure, 3: Misc, 4: Recursion | difficulty: 1 ~ 5
    public void addQuestion(String question, String answer, Category category, int difficulty) {
        String uniqueID;
        // Make sure each ID is indeed unique
        do {
            uniqueID = categoryCharacter.get(category) + difficulty + UUID.randomUUID().toString();
        } while (questionSets.containsKey(uniqueID));
        questionSets.put(uniqueID, new String[]{question, answer});
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
                Category.DataStructure, 1
        );
    }

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
