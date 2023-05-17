package com.example.myeducationalapp;

import android.graphics.Color;

import com.example.myeducationalapp.SyntaxHighlighting.DetectCodeBlock;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author u7469758 Geun Yun
 * @author u7468248 Alex Boxall
 * @author u7300256 Nikhila Gurusinghe
 */
public class QuestionSet {


    public enum Category {
        Algorithm, ControlFlow, DataStructure, Miscellaneous, Recursion, TestQuestion;

        private final int[] primaryCategoryColorList = new int[] {
                Color.parseColor("#FFF25260"),
                Color.parseColor("#FFB0C5FF"),
                Color.parseColor("#FF7549D2"),
                Color.parseColor("#FF00BFAF"),
                Color.parseColor("#FFFF598F"),

        };

        private final int[] secondaryCategoryColorList = new int[] {
                Color.parseColor("#FFFD99A2"),
                Color.parseColor("#FF7896E8"),
                Color.parseColor("#FF9767FE"),
                Color.parseColor("#FF108A80"),
                Color.parseColor("#FFFF92B6"),

        };

        private final int[] tertiaryCategoryColorList = new int[] {
                Color.parseColor("#FFFF5659"),
                Color.parseColor("#FF425381"),
                Color.parseColor("#FFC4A9FE"),
                Color.parseColor("#FF14B0A3"),
                Color.parseColor("#FFFF94B7")

        };

        public int getPrimaryCategoryColor() {
            assert this != TestQuestion;

            return primaryCategoryColorList[this.ordinal()];
        }

        public int getSecondaryCategoryColor() {
            assert this != TestQuestion;

            return secondaryCategoryColorList[this.ordinal()];
        }

        public int getTertiaryCategoryColor() {
            assert this != TestQuestion;

            return tertiaryCategoryColorList[this.ordinal()];
        }

        public int getCategoryImageDrawableID() {
            assert this != TestQuestion;

            switch (this.ordinal()) {
                case 0:
                    return R.drawable.category_image_algorithm;
                case 1:
                    return R.drawable.category_image_control_flow;
                case 2:
                    return R.drawable.category_image_data_structure;
                case 3:
                    return R.drawable.category_image_miscellaneous;
                case 4:
                    return R.drawable.category_image_recursion;
            }

            return 0;
        }



        @Override
        public String toString() {
            switch (this.ordinal()) {
                case 1:
                    return "Control Flow";
                case 2:
                    return "Data Structure";
                case 5:
                    return "Test Question";
                default:
                    return super.toString();
            }

        }
    }

    // global maximum number of multiple choice options
    // This should probably be less than there are letters in the alphabet :)
    public static final int MAXIMUM_NUMBER_OF_MULTI_CHOICE_OPTIONS = 6;

    // <Question ID, String[]{Question, answer} pair
    // MUST BE SORTED - otherwise the challenge of the day will be inconsistent
    private SortedMap<String, Question> usedQuestionSets;

    private QuestionSet() {
        usedQuestionSets = new TreeMap<>();

        addDataStructure();
        addRecursion();
        addMiscellaneous();
        addAlgorithms();
        addControlFlow();

        /*
         * Required for certain unit tests.
         */
        addTestQuestion("Q");
        addTestQuestion("Q1");
        addTestQuestion("Q2");
        addTestQuestion("Q3");
        addTestQuestion("R");

    }

    private void addTestQuestion(String id) {
        Question testQuestion = new Question("Q", "Q", "Q", Category.TestQuestion, 3);
        testQuestion.setID(id);
        addQuestion(testQuestion);
    }

    static private QuestionSet instance;

    static public QuestionSet getInstance() {
        if (instance == null) {
            instance = new QuestionSet();
        }
        return instance;
    }

    public List<String> getQuestionIDsInCategory(Category category) {
        return usedQuestionSets.keySet().stream().filter((x) -> usedQuestionSets.get(x).getCategory() == category).collect(Collectors.toList());
    }

    public int getNumberOfQuestionsInCategory(Category category) {
        return getQuestionIDsInCategory(category).size();
    }

    public Question getQuestionFromID(String id) {
        return usedQuestionSets.get(id);
    }

    public Set<String> getQuestionIDs() {
        return usedQuestionSets.keySet();
    }

    // Type 0: Algo, 1: ControlFlow, 2: DataStructure, 3: Misc, 4: Recursion | difficulty: 1 ~ 5
    // difficulty is in String so that category + difficulty does not give sum of ascii values of characters
    public void addQuestion(Question question) {
        usedQuestionSets.put(question.getID(), question);
    }

    /**
     * Gets the question of the day where the category rotates every day.
     */
    public Question getQuestionOfTheDay() {
        /*
         * In UTC time.
         */
        long millisSince1970 = System.currentTimeMillis();

        /*
         * Add 10 hours so it occurs at midnight in the +10 timezone (Canberra).
         */
        millisSince1970 += 1000 * 60 * 60 * 10;
        int day = (int) (millisSince1970 / (1000 * 60 * 60 * 24));
        List<String> possibleQuestions = getQuestionIDsInCategory(Category.values()[(day % 5)]);
        return usedQuestionSets.get(possibleQuestions.get((day / 5) % possibleQuestions.size()));
    }

    private void addAlgorithms() {
        addQuestion(new Question(
                "(A) PLEASE WRITE ME",
                "Please write this!",
                "Please write this!",
                Category.Algorithm,
                1
        ));
    }

    private void addControlFlow() {
        addQuestion(new Question(
                "If you please",
                "Which letter gets printed to the screen?" +
                        "```" +
                        "String x = \"yes please\";\n" +
                        "if (x.equals(\"please\") {\n" +
                        "    System.out.println(\"A\");\n" +
                        "} else if (x.contains(\"please\") {\n" +
                        "    System.out.println(\"B\");\n" +
                        "} else {\n" +
                        "    System.out.println(\"C\");\n" +
                        "}",
                "B",
                Category.ControlFlow,
                1
        ));

        addQuestion(new Question(
                "For four fours",
                "What will be the value of x after the execution of the below code?\n" +
                        "```" +
                        "int x = 4;\n" +
                        "for (int i = 4; i >= 0; --i) {\n" +
                        "    x += i * 4 + 4;\n" +
                        "}\n" +
                        "```\n",
                "64",
                Category.ControlFlow,
                2
        ));

        addQuestion(new Question(
                "Breaking expectations",
                "What will be the value of x after the execution of the below code?\n" +
                        "```" +
                        "int x = 5;\n" +
                        "switch (x) {\n" +
                        "   case 7:\n" +
                        "       x = 5;\n" +
                        "       break;\n" +
                        "   case 6:\n" +
                        "   case 5:\n" +
                        "       x -= 1;\n" +
                        "   case 123:\n" +
                        "   case 345:\n" +
                        "       x *= 2;\n" +
                        "       break;\n" +
                        "   case 4:\n" +
                        "       x = 15;\n" +
                        "   default:\n" +
                        "       break;\n" +
                        "}",
                "8",
                Category.ControlFlow,
                2
        ));

        addQuestion(new Question(
                "Do while",
                "What is the value of y after the loop ends?\n" +
                "```int x = 0;\n" +
                        "int y = 0;\n" +
                        "do {\n" +
                        "    x = 1 - x;\n" +
                        "    ++y;\n" +
                        "} while (x != 0);\n",
                "2",
                Category.ControlFlow,
                1
        ));
    }

    private void addDataStructure() {
        // Question about HashMap
        addQuestion(new Question("Maps full of hashes",
                "What will be the value returned by map.get(2) after the execution of the code below?\n\n"+
                        "```Map<Integer, String> map = new HashMap<Integer, String>();\n"+
                        "map.put(1, \"2\");\n"+
                        "map.put(2, \"5\");\n"+
                        "map.put(2, \"-1\");\n"+
                        "map.put(-1, \"6\");```\n\n" +
                        "A) 1 B) 2 C) 5 D) -1 E) 6",
                "D", Category.DataStructure, 1));

        // Question about LinkedList
        addQuestion(new Question("Going backwards",
                "What is the time complexity of the reverse()` method below?\n\n" +
                        "```public void reverse() {\n" +
                        "    Node<T> prev = null;\n" +
                        "    Node<T> current = head;\n" +
                        "    while (current != null) {\n" +
                        "        Node<T> next = current.next;\n" +
                        "        current.next = prev;\n" +
                        "        prev = current;\n" +
                        "        current = next;\n" +
                        "    }\n" +
                        "    head = prev;\n" +
                        "}```\n\n" +
                        "A) O(1) B) O(n) C) O(n^2) D) O(log n) E) O(n log n)",
                "B", QuestionSet.Category.DataStructure, 2));

        // Question about BinaryTree
        addQuestion(new Question("Straightening out trees",
                "What will be the output of the following code?\n\n" +
                        "```BinaryTree tree = new BinaryTree(1);\n" +
                        "tree.left = new BinaryTree(2);\n" +
                        "tree.right = new BinaryTree(3);\n" +
                        "tree.left.left = new BinaryTree(4);\n" +
                        "tree.left.right = new BinaryTree(5);\n" +
                        "tree.right.left = new BinaryTree(6);\n" +
                        "tree.right.right = new BinaryTree(7);\n\n" +
                        "List<Integer> result = tree.inOrderTraversal();\n" +
                        "System.out.println(result)```;\n\n" +
                        "A) [1, 2, 4, 5, 3, 6, 7] " +
                        "B) [4, 2, 5, 1, 6, 3, 7] " +
                        "C) [4, 5, 2, 6, 7, 3, 1] " +
                        "D) [1, 2, 3, 4, 5, 6, 7] " +
                        "E) [7, 6, 3, 5, 4, 2, 1]",
                "A", QuestionSet.Category.DataStructure, 3));

        // Question about stack
        addQuestion(new Question("Stack the weirdness",
                "What is the result of weirdCalculator(\"234*+5+\")?\n\n " +
                        "```public static int weirdCalculator(String expression) {\n" +
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
                        "}```\n\n" +
                        "A) 10 B) 11 C) 12 D) 15 E) 19",
                "E", QuestionSet.Category.DataStructure, 4));

        // Question about BST
        addQuestion(new Question("Mind-bending trees",
                "Given an integer n, what is the number (G(n)) of structurally unique BSTs \n" +
                        "which has exactly n nodes of unique values from 1 to n?\n" +
                        "Initial State: G(0) = 1 G(1) = 1\n\n" +
                        "A) G(n) = SUM(G(i - 1) * G(n - i)) over i = 1, ..., n - 1 \n" +
                        "B) G(n) = SUM(G(i - 1) * G(n - i)) over i = 1, ..., n \n" +
                        "C) G(n) = SUM(G(i) * G(n - i)) over i = 1, ..., n \n" +
                        "D) G(n) = SUM(G(i - 1) * G(n - 1)) over i = 1, ..., n - 1 \n" +
                        "E) G(n) = SUM(G(i) * G(n - i)) over i = 1, ..., n - 1 \n",
                "B" ,QuestionSet.Category.DataStructure, 5));
    }

    private void addMiscellaneous() {
        // Octal literals
        addQuestion(new Question("Many formats of integers",
                "What is the output of this line of code?\n" +
                        "```System.out.printf(\"%03X%03X\", 050, 100);    \n```"
        , "028064", Category.Miscellaneous, 3));

        // XOR swapping
        addQuestion(new Question("Funny XORs",
                "What is the output of this section of code?\n" +
                        "```int a = 30;\n" +
                        "int b = 45;\n" +
                        "a ^= b;\n" +
                        "b ^= a;\n" +
                        "a ^= b;\n" +
                        "System.out.printf(\"%d, %d\", a, b);```"
                , "45, 30", Category.Miscellaneous, 3));

        // XOR swapping gone wrong
        addQuestion(new Question("Strange XORs",
                "What is the output of this section of code?\n" +
                        "```int a = 45;\n" +
                        "int b = 45;\n" +
                        "a ^= b;\n" +
                        "b ^= a;\n" +
                        "a ^= b;\n" +
                        "System.out.printf(\"%d, %d\", a, b);    ```"
                , "0, 0", Category.Miscellaneous, 2));

        // Weirdo bitwise manipulation
        addQuestion(new Question("Fiddling with bits",
                "What is the value of y?\n" +
                        "```int x = 1;\n" +
                        "int y = ((x & (-2)) ^ (-1)) + 1;```"
        , "0", Category.Miscellaneous, 4));

        // Labels
        addQuestion(new Question("The web of lies",
                "Will this of piece of Java code compile (assuming it is placed within a valid class)?\n" +
                        "If not, on which line is the first syntax error?\n" +
                        "```public void method() {                  // line 1\n" +
                        "    https://www.google.com              // line 2\n" +
                        "    http://www.anusolarracing.com       // line 3     \n" +
                        "    https://www.example.com             // line 4\n" +
                        "    www.example.com                     // line 5\n" +
                        "    ();                                 // line 6\n" +
                        "}\n```" +
                        "A) Yes \n" +
                        "B) No, the first error occurs on line 2 \n" +
                        "C) No, the first error occurs on line 3 \n" +
                        "D) No, the first error occurs on line 4 \n" +
                        "E) No, the first error occurs on line 5 \n" +
                        "F) No, the first error occurs on line 6 \n"
                , "D", Category.Miscellaneous, 5));

    }




    private void addRecursion() {
        addQuestion(new Question("Recursion basics",
                "What value does adder(0) return?\n" +
                        "```public static int adder(int i) { \n"+
                        "    if (i==5) return i;\n" +
                        "    else return 1 + adder(i+1);\n" +
                        "}```"
                , "10", Category.Recursion, 1));


        addQuestion(new Question("Recursion and lists",
                "What value does recursion(0) return?\n" +
                        "```static int[] numbers = new int[]{\n" +
                        "    11,5,89,2,7,8,12,4,5,37\n" +
                        "};\n" +
                        "public static int recursion(int i) {\n" +
                        "    if (i == numbers.length - 1) return 0;\n" +
                        "    if (numbers[i] % 2 == 0) return i + recursion(i + 1);\n" +
                        "    else return recursion(i + 1);\n" +
                        "}```"
                , "21", Category.Recursion, 2));


        addQuestion(new Question("Lists and recursion",
                "What is the contents of ints after calling list(0)?\n" +
                        "```static int[] ints = new int[] {\n" +
                        "    17,5,7,3,9,1,11,15,13\n" +
                        "};\n" +
                        "public static void list(int i) {\n" +
                        "    if (i == ints.length - 1) return;\n" +
                        "    if (ints[i] < ints[i+1]) list(i+1);\n" +
                        "    else {\n" +
                        "        int j = ints[i];\n" +
                        "        ints[i] = ints[i+1];\n" +
                        "        ints[i+1] = j;\n" +
                        "        list(i+1);\n" +
                        "    }\n" +
                        "}```\n" +
                        "A) [17,5,7,3,9,1,11,15,13] \n" +
                        "B) [1,3,5,7,9,11,13,15,17] \n" +
                        "C) [17,15,13,11,9,7,5,3,1] \n" +
                        "D) [5,7,3,9,1,11,15,13,17] \n" +
                        "E) [5,7,3,1,9,11,13,15,17] "
                , "D", Category.Recursion, 3));



        addQuestion(new Question("The tree of recursion",
                "What is the result of an in-order traversal on the tree generated by treeRecursion(root, 1)?\n" +
                        "```public static void treeRecursion(Node node, int i) {\n" +
                        "    if (i > 10) return;\n" +
                        "    i = (i * 3)/2;\n" +
                        "    if (i % 2 == 0) {\n" +
                        "        node.left = new Node(i);\n" +
                        "        treeRecursion(node.left,i);\n" +
                        "    }\n" +
                        "    else {\n" +
                        "        node.right = new Node(i);\n" +
                        "        treeRecursion(node.right, i);\n" +
                        "    }\n" +
                        "}```\n" +
                        "A) [2,3,12,8,5,1] \n" +
                        "B) [1,2,3,5,8,12] \n" +
                        "C) [1,1.5,2.25,3.375,5.0625,7.59375,11.390625] \n" +
                        "D) [1.5,2.25,3.375,11.390625,7.59375,5.0625,1] \n" +
                        "E) [12,8,3,2,1,5]\n"
                , "A", Category.Recursion, 5));




    }

    public SortedMap<String, Question> getUsedQuestionSets() {
        return usedQuestionSets;
    }
}
