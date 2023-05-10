package com.example.myeducationalapp.Search.SearchResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionResults extends Results{
    private ArrayList<SearchResult> searchResults = new ArrayList<>();
    @Override
    public List<SearchResult> results(List<String> search) {
        updateQuestions();

        //TODO

        return searchResults;
    }

    public List<SearchResult> looseResults(List<String> search) {
        updateQuestions();

        searchResults.forEach(searchResult -> {
            int confidence = 0;
            for (var searchWord : search) {
                for (var questionWord : searchResult.getWords()) {
                    if (Objects.equals(searchWord, questionWord)) confidence++;
                }
            }

            searchResult.setConfidence(confidence);
        });

        return searchResults;
    }

    /**
     * Updates the array to store all questions as objects
     */
    void updateQuestions() {
        //var questions =

        //var searchResult = new SearchResult(/*Str*/, SearchToken.Query.Question, /*words*/);



//        FirebaseResult questionsResult = Firebase.getInstance().readAllQuestions();
//        List<String> questionArray = (List<String>)questionsResult.await();
//        for (var question : questionArray) {
//            var words = Arrays.asList(question.split(" "));
//            var id = 0;//TODO
//            var searchResult = new SearchResult(id, SearchToken.Query.Question, words);
//            searchResults.add(searchResult);
//        }
    }
}
