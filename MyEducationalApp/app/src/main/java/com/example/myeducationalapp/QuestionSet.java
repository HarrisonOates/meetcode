package com.example.myeducationalapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

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
        /*
         * This needs to be consistent, and so we should seed the random number generator
         * before calling it. For it to change each day, we need the seed to change each
         * day. The easiest way of doing that is just using the day itself.
         */
        long millisSince1970 = System.currentTimeMillis();
        long day = millisSince1970 / (1000 * 60 * 60 * 24);
        Random rng = new Random(day);

        categoryIndex = (categoryIndex + 1) % 5;
        String uniqueID;
        do {
            List<String> keys = new ArrayList<>(unusedQuestionSets.keySet());
            Collections.shuffle(keys, rng);
            uniqueID = keys.get(0);
        } while (uniqueID.charAt(0) != categoryRotation[categoryIndex]);
        String[] question = unusedQuestionSets.get(uniqueID);
        unusedQuestionSets.remove(uniqueID);
        usedQuestionSets.put(uniqueID, question);
        return question;

    }
    // I think we can just call one of these questions once a day?
    public void dataStructure1() {
        // Question about HashMap
        addQuestion(
                "What will be the output of map.get(2) after the execution of the codes below?\n\n"+
                        "Map<Integer, String> map = new HashMap<Integer, String>();\n"+
                        "map.put(1, \"2\");\n"+
                        "map.put(2, \"5\");\n"+
                        "map.put(2, \"-1\");\n"+
                        "map.put(-1, \"6\");\n\n" +
                        "A) 1, B) 2, C) 5, D) -1, E) 6",
                "D", Category.DataStructure, "1");
    }

    // TODO: add some questions and is this the structure of QuestionSet we want to use?
    public void dataStructure2() {
        // Question about LinkedList
        addQuestion(
                "What is the time complexity of the `reverse()` method in the `LinkedList`?\n\n" +
                        "public void reverse() {\n" +
                        "        Node<T> prev = null;\n" +
                        "        Node<T> current = head;\n" +
                        "        while (current != null) {\n" +
                        "            Node<T> next = current.next;\n" +
                        "            current.next = prev;\n" +
                        "            prev = current;\n" +
                        "            current = next;\n" +
                        "        }\n" +
                        "        head = prev;\n" +
                        "    }\n\n" +
                        "A) O(1), B) O(n), C) O(n^2), D) O(log n), E) O(n log n)",
                "B", QuestionSet.Category.DataStructure, "2");
    }
    public void dataStructure3() {
        // Question about BinaryTree
         addQuestion(
                 "What will be the output of the following code?\n\n" +
                         "BinaryTree tree = new BinaryTree(1);\n" +
                         "tree.left = new BinaryTree(2);\n" +
                         "tree.right = new BinaryTree(3);\n" +
                         "tree.left.left = new BinaryTree(4);\n" +
                         "tree.left.right = new BinaryTree(5);\n" +
                         "tree.right.left = new BinaryTree(6);\n" +
                         "tree.right.right = new BinaryTree(7);\n\n" +
                         "List<Integer> result = tree.inOrderTraversal();\n" +
                         "System.out.println(result);\n\n" +
                         "A) [1, 2, 4, 5, 3, 6, 7]\n" +
                         "B) [4, 2, 5, 1, 6, 3, 7]\n" +
                         "C) [4, 5, 2, 6, 7, 3, 1]\n" +
                         "D) [1, 2, 3, 4, 5, 6, 7]\n" +
                         "E) [7, 6, 3, 5, 4, 2, 1]",
                 "A", QuestionSet.Category.DataStructure, "3");
    }

    public void dataStructure4() {
        // Question about stack
        addQuestion(
                "What is the result of weirdCalculator(\"234*+5+\")?\n\n " +
                        "public static int weirdCalculator(String expression) {\n" +
                        "   Stack<Integer> stack = new Stack<>();\n" +
                        "   for (int i = 0; i < expression.length(); i++) {\n" +
                        "       char c = expression.charAt(i);\n" +
                        "       if (Character.isDigit(c)) {\n" +
                        "           stack.push(c - '0');\n" +
                        "       } else {\n" +
                        "           int operand2 = stack.pop();\n" +
                        "           int operand1 = stack.pop();\n" +
                        "           int result = 0;\n" +
                        "           switch (c) {\n" +
                        "               case '+':\n" +
                        "                   result = operand1 + operand2;\n" +
                        "                   break;\n" +
                        "               case '-':\n" +
                        "                   result = operand1 - operand2;\n" +
                        "                   break;\n" +
                        "               case '*':\n" +
                        "                   result = operand1 * operand2;\n" +
                        "                   break;\n" +
                        "               case '/':\n" +
                        "                   result = operand1 / operand2;\n" +
                        "                   break;\n" +
                        "           }\n" +
                        "           stack.push(result);\n" +
                        "       }\n" +
                        "   }\n" +
                        "   return stack.pop();\n" +
                        "}\n\n" +
                        "A) 10, B) 11, C) 12, D) 15, E) 19",
                "E", QuestionSet.Category.DataStructure, "4");
    }

    public void dataStructure5() {
        // Question about BST
        addQuestion(
                "Given an integer n, what is the number (G(n)) of structurally unique BST's " +
                        "which has exactly n nodes of unique values from 1 to n?\n" +
                        "Initial State: G(0) = 1 G(1) = 1\n\n" +
                        "A) G(n) = SUM(G(i - 1) * G(n - i)) over i = 1, ..., n - 1\n" +
                        "B) G(n) = SUM(G(i - 1) * G(n - i)) over i = 1, ..., n\n" +
                        "C) G(n) = SUM(G(i) * G(n - i)) over i = 1, ..., n\n" +
                        "D) G(n) = SUM(G(i - 1) * G(n - 1)) over i = 1, ..., n - 1\n" +
                        "E) G(n) = SUM(G(i) * G(n - i)) over i = 1, ..., n - 1",
                "B" ,QuestionSet.Category.DataStructure, "5");
    }


    public void addMiscellaneous() {
        // Octal literals
        addQuestion(
                "What is the output of this line of code?" +
                        "    System.out.printf(\"%03X%03X\", 050, 100);"
        , "028064", Category.Miscellaneous, "3");

        // XOR swapping
        addQuestion(
                "What is the output of this section of code?" +
                        "    int a = 30;" +
                        "    int b = 45;" +
                        "    a ^= b;" +
                        "    b ^= a;" +
                        "    a ^= b;" +
                        "    System.out.printf(\"%d, %d\", a, b);"
                , "45, 30", Category.Miscellaneous, "3");

        // XOR swapping gone wrong
        addQuestion(
                "What is the output of this section of code?" +
                        "    int a = 45;" +
                        "    int b = 45;" +
                        "    a ^= b;" +
                        "    b ^= a;" +
                        "    a ^= b;" +
                        "    System.out.printf(\"%d, %d\", a, b);"
                , "0, 0", Category.Miscellaneous, "3");

        // Weirdo bitwise manipulation
        addQuestion(
                "What is the value of y?" +
                        "    int x = 1" +
                        "    int y = ((x & (-2)) ^ (-1)) + 1;"
        , "0", Category.Miscellaneous, "4");

        // Labels
        addQuestion(
                // THIS QUESTION NEEDS SYNTAX HIGHLIGHTING DISABLED FOR IT TO WORK PROPERLY
                "Will this of piece of Java code compile (assuming it is placed within a valid class)?" +
                        "public void method() {                  // line 1" +
                        "    https://www.google.com              // line 2" +
                        "    http://www.anusolarracing.com       // line 3" +
                        "    https://www.example.com             // line 4" +
                        "    www.example.com                     // line 5" +
                        "    ();                                 // line 6" +
                        "}" +
                        "A: Yes" +
                        "B: No, the first error occurs on line 2" +
                        "C: No, the first error occurs on line 3" +
                        "D: No, the first error occurs on line 4" +
                        "E: No, the first error occurs on line 5" +
                        "F: No, the first error occurs on line 6"
                , "D", Category.Miscellaneous, "5");

    }




    public void addRecursion() {
        addQuestion(
                "What is the output of adder(0)" +
                        "public static int adder(int i) { "+
                        "    if (i==5) return i;" +
                        "    else return 1 + adder(i+1);" +
                        "}"
                , "10", Category.Recursion, "1");


        addQuestion(
                "What is the output of recursion(0)" +
                        "static int[] numbers = new int[]{11,5,89,2,7,8,12,4,5,37};" +
                        "public static int recursion(int i) {" +
                        "    if (i == numbers.length - 1) return 0;" +
                        "    if (numbers[i] % 2 == 0) return i + recursion(i + 1);" +
                        "    else return recursion(i + 1);" +
                        "}"
                , "21", Category.Recursion, "2");


        addQuestion(
                "What is the contents of ints after calling list(0)" +
                        "static int[] ints = new int[] {17,5,7,3,9,1,11,15,13};" +
                        "public static void list(int i) {" +
                        "    if (i == ints.length - 1) return;" +
                        "    if (ints[i] < ints[i+1]) list(i+1);" +
                        "    else {" +
                        "        int j = ints[i];" +
                        "        ints[i] = ints[i+1];" +
                        "        ints[i+1] = j;" +
                        "        list(i+1);" +
                        "    }" +
                        "}" +
                        "A) [17,5,7,3,9,1,11,15,13]\n" +
                        "B) [1,3,5,7,9,11,13,15,17]\n" +
                        "C) [17,15,13,11,9,7,5,3,1]\n" +
                        "D) [5,7,3,9,1,11,15,13,17]\n" +
                        "E) [5,7,3,1,9,11,13,15,17]"
                , "D", Category.Recursion, "3");



        addQuestion(
                "What is the result of an in-order traversal on the tree generated by treeRecursion(root, 1)" +
                        "public static void treeRecursion(Node node, int i) {" +
                        "    if (i > 10) return;" +
                        "    i = (i * 3)/2;" +
                        "    if (i % 2 == 0) {" +
                        "        node.left = new Node(i);" +
                        "        treeRecursion(node.left,i);" +
                        "    }" +
                        "    else {" +
                        "        node.right = new Node(i);" +
                        "        treeRecursion(node.right, i);" +
                        "    }" +
                        "}" +
                        "A) [2,3,12,8,5,1]\n" +
                        "B) [1,2,3,5,8,12]\n" +
                        "C) [1,1.5,2.25,3.375,5.0625,7.59375,11.390625]\n" +
                        "D) [1.5,2.25,3.375,11.390625,7.59375,5.0625,1]\n" +
                        "E) [12,8,3,2,1,5]"
                , "A", Category.Recursion, "5");




    }

    public HashMap<String, String[]> getUsedQuestionSets() {
        return usedQuestionSets;
    }
}
