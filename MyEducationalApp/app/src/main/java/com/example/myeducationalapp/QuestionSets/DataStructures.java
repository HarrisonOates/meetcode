package com.example.myeducationalapp.QuestionSets;
import com.example.myeducationalapp.SyntaxHighlighting.*;

public class DataStructures extends QuestionSet{
    public DataStructures(String question, String answer, char difficulty) {
        super(question, answer, '2', difficulty);
    }

    public DataStructures dataStructureQ1 = new DataStructures(
            "What will be the output of map.get(2) after the execution of the codes below? \n" +
                    "A) 1, B) 2, C) 5, D) -1, E) 6 \n\n" +
                    "Map<Integer, String> map = new HashMap<Integer, String>(); \n" +
                    "map.put(1, \"2\"); \n" +
                    "map.put(2, \"5\"); \n" +
                    "map.put(2, \"-1\"); \n" +
                    "map.put(-1, \"6\");",
            "D",
            '1');

    // Question 2:
    // Answer 2:

    // Question 3:
    // Answer 3:

    // Question 4:
    // Answer 4:

    // Question 5:
    // Answer 5:
}
