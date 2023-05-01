package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseResult;
import com.example.myeducationalapp.Search.SearchToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class QuestionResults extends Results{

    static List<String> array = new ArrayList<>();
    @Override
    public List<SearchResult> results(List<String> search) {
        updateQuestions();

        //TODO

        return null;
    }

    public List<SearchResult> looseResults(List<String> search) {
        updateQuestions();

        ArrayList<SearchResult> searchResults = new ArrayList<>();

        array.forEach(x -> {
            int confidence = 0;
            ArrayList<String> words = new ArrayList<>(Arrays.asList(x.split(" ")));
            for (var searchWord : search) {
                for (var questionWord : words) {
                    if (Objects.equals(searchWord, questionWord)) confidence++;
                }
            }

            searchResults.add(new SearchResult(0/**TODO**/, SearchToken.Query.Question,confidence));
        });



        return searchResults;
    }

    /**
     * Updates the array to store all questions as strings
     */
    void updateQuestions() {
        FirebaseResult questionsResult = Firebase.getInstance().readAllQuestions();
        array = (List<String>)questionsResult.await();
    }
}
