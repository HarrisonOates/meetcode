package com.example.myeducationalapp.QuestionSets;
import com.example.myeducationalapp.SyntaxHighlighting.*;

import java.util.HashMap;
import java.util.UUID;

public abstract class QuestionSet {
    // <Question ID, String[]{Question, answer} pair
    public HashMap<String, String[]> questionSets;

    // Type 0: Algo, 1: ControlFlow, 2: DataStructure, 3: Misc, 4: Recursion | difficulty: 1 ~ 3
    public QuestionSet(String question, String answer, char type, char difficulty) {
        String uniqueID = UUID.randomUUID().toString();
        uniqueID = type + difficulty + uniqueID;
        questionSets.put(uniqueID, new String[]{question, answer});

    }

}
