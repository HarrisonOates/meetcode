package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Search.SearchParsing.SearchToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TopicResults extends Results{
    String[] topics = new String[]{"Algorithm", "ControlFlow", "DataStructure", "Miscellaneous", "Recursion"};

    @Override
    public List<SearchResult> results(List<String> search) {
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
        double sum = 0;

        for (var character : word.toCharArray()) {
            var c = Character.toLowerCase(character);
            if (map.containsKey(c)) map.put(c,map.get(c)-1);
            else sum -= 1.5; //Letters in search, not in topic
        }

        for (var entry : map.keySet()) {
            var num = map.get(entry);
            sum -= num > 0 ? num * 0.1 : -num; //> 0 = in topic, not in search. < 0 = letters more freq in search
        }

        return sum * (1+Math.abs(word.length() - length)*0.07);
    }
}
