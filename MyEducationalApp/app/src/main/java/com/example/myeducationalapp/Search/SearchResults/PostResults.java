package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Search.SearchParsing.SearchToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PostResults extends Results{
    private ArrayList<SearchResult> searchResults = new ArrayList<>();
    HashMap<Character, Double> topics = new HashMap<>(Map.of('0',1.0,'1',1.0,'2',1.0,'3',1.0,'4',1.0));

    @Override
    public List<SearchResult> results(List<String> search) {
        return null;
    }

    @Override
    public List<SearchResult> looseResults(List<String> search) {
    //updatePosts();
        if (searchResults == null) updatePosts();

        searchResults.forEach(searchResult -> {
        int confidence = 0;
        for (var searchWord : search) {
            for (var questionWord : searchResult.getWords()) {
                if (Objects.equals(searchWord, questionWord)) confidence++;
                else if (questionWord.contains(searchWord)) confidence += 0.5;
            }
        }

            var multiplier = topics.get(searchResult.getId().charAt(0));

            searchResult.setConfidence(multiplier * confidence);
    });

        return searchResults;
    }


    //TODO: When is this updated (as with other sections)
    //TODO: Filter topics
    public void updatePosts() {
        //var questions = (QuestionSet).geUsedQuestions();
        HashMap<String, String[]> questions = new HashMap<>();
        //TODO

        if (questions == null) return;

        var entrySet = questions.keySet();
        for (String entry : entrySet) {
            //TODO: Filter
            if (!topics.containsKey(entry.charAt(0))) continue;


            var comments = (String)Firebase.getInstance().readQuestionComments(entry).await();
            var splitComments = comments.split("\n");
            int index = 0;

            for (var comment : splitComments) {
                var sections = comment.split("\t");
                String id = entry + "\n" + index;//sections[0]; //TODO indexWithinThread
                List<String> words = List.of(sections[1].split(" "));

                var searchResult = new SearchResult(id, SearchToken.Query.Discussion, words);
                searchResults.add(searchResult);
                index++;
            }
        }
    }

    public void setTopics(HashMap<Character, Double> topics) {
        this.topics = topics;
    }
}
