package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Search.SearchToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TopicResults extends Results{
    String[] topics = new String[]{"Algorithm", "ControlFlow", "DataStructure", "Miscellaneous", "Recursion"};
    @Override
    public List<SearchResult> results(List<String> search) {
        return null;
    }

    @Override
    public List<SearchResult> looseResults(List<String> search) {
        List<SearchResult> results = new ArrayList<>();

        for (var topic : topics) {
            var map = new HashMap<Character, Integer>();
            var charArray = topic.toCharArray();
            for (var character : charArray) {
                var c = Character.toLowerCase(character);
                if (!map.containsKey(c)) map.put(c,1);
                else map.put(c, map.get(c)+1);
            }

            double score = -Integer.MIN_VALUE;
            var totalWord = "";

            for (var searchWord : search) {
                totalWord += searchWord;
                var currentScore = wordScore(searchWord, (HashMap<Character, Integer>) map.clone(), charArray.length);
                if (currentScore > score) score = currentScore;
            }

            var currentScore = wordScore(totalWord, (HashMap<Character, Integer>) map.clone(), charArray.length);
            if (currentScore > score) score = currentScore;



            var currentResult = new SearchResult(topic, SearchToken.Query.Topic, Collections.singletonList(topic));
            currentResult.setConfidence(score);
            results.add(currentResult);
        }


        return results;
    }



    double wordScore(String word, HashMap<Character, Integer> map, double length) {
        for (var character : word.toCharArray()) {
            var c = Character.toLowerCase(character);
            if (map.containsKey(c)) map.put(c,map.get(c)-1);
        }

        double sum = 0;
        for (var entry : map.keySet()) {
            var num = map.get(entry);
            sum -= Math.abs(num);
        }

        return sum / (Math.abs(word.length() - length)*0.2 + 1);
    }
}
