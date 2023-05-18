package com.example.myeducationalapp.Question;

/**
 * Stores a programming question that users can answer in the app.
 *
 * @author u7468248 Alex Boxall
 */
public class Question {
    private String name;
    private String content;
    private String answer;
    private int difficulty;
    private QuestionSet.Category category;
    private String id;

    Question(String name, String content, String answer, QuestionSet.Category category, int difficulty) {
        this.name = name;
        this.content = content;
        this.answer = answer;
        this.difficulty = difficulty;
        this.category = category;
        this.id = category + "_" + difficulty + "_" + content.hashCode() + name.hashCode() + answer.hashCode();
    }

    /*
     * Only used to set up certain unit tests.
     */
    protected void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getAnswer() {
        return answer;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public QuestionSet.Category getCategory() {
        return category;
    }
}
