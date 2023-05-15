package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Search.SearchParsing.SearchToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuestionResults extends Results{
    private ArrayList<SearchResult> searchResults = new ArrayList<>();
    HashMap<Character, Double> topics = new HashMap<>(Map.of('0',1.0,'1',1.0,'2',1.0,'3',1.0,'4',1.0));

    @Override
    public List<SearchResult> results(List<String> search) {
        //updateQuestions();

        //TODO

        return searchResults;
    }

    public List<SearchResult> looseResults(List<String> search) {
        //updateQuestions();
        if (searchResults == null) updateQuestions();

        searchResults.forEach(searchResult -> {
            int confidence = 0;
            for (var searchWord : search) {
                for (var questionWord : searchResult.getWords()) {
                    if (Objects.equals(searchWord, questionWord)) confidence++;
                    else if (questionWord.contains(searchWord)) confidence += 0.5;
                }
            }

            var multiplier =

            searchResult.setConfidence(multiplier * confidence);
        });

        return searchResults;
    }

    /**
     * Updates the array to store all questions as objects
     */
    public void updateQuestions() {
        //var questions = (QuestionSet).geUsedQuestions();
        HashMap<String, String[]> questions = new HashMap<>();
        //TODO

        if (questions == null) return;

        var entrySet = questions.keySet();
        for (String entry : entrySet) {
            if (!topics.containsKey(entry.charAt(0))) continue;//Arrays.stream(topics.).noneMatch(x -> x == entry.charAt(0))) continue;

            var searchResult = new SearchResult(entry, SearchToken.Query.Question, List.of(questions.get(entry)[0].split(" ")));
            searchResults.add(searchResult);
        }
    }


    public void setTopics(HashMap<Character, Double> topics) {
        this.topics = topics;
    }
}
