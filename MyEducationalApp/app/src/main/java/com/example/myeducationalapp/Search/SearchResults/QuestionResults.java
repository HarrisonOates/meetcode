package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Firebase.Firebase;

import java.util.ArrayList;
import java.util.List;

public class QuestionResults extends Results{

    static ArrayList<String> array = new ArrayList<>();
    @Override
    public List<SearchResult> results() {
//        var results = Firebase.getInstance().readAllQuestions();
//        results.then(x -> array.add((String) x));
//        System.out.println(array);


        return null;
    }

    public static void main(String[] args) {
        var results = Firebase.getInstance().readAllQuestions();
        results.then(x -> array.add((String) x));
        System.out.println(array);
    }
}
