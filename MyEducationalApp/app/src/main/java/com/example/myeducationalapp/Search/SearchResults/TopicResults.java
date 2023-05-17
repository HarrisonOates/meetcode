package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Search.SearchParsing.SearchToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Obtains the results of how likely each topic is to be the result being searched for
 * @author Jayden
 */
public class TopicResults extends Results{
    /**
     * A list of all the topics in the app
     */
    String[] topics = new String[]{"Algorithm", "ControlFlow", "DataStructure", "Miscellaneous", "Recursion"};

    /**
     * Returns a list containing the results from a topic search
     * @param search The user inputted search
     * @return A list containing results from each topic
     */
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


    /**
     * Calculates the relative score of a word, indicating how likely it is to match the search result
     * @param word A word from the search
     * @param map A map of all the characters from the current topic, and the occurrence of each character
     * @param length The length of the topic word
     * @return A relative score of how close a searched word is to the user name
     */
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
