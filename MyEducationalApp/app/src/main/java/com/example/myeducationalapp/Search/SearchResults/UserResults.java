package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Search.SearchParsing.SearchToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UserResults extends Results{
    @Override
    public List<SearchResult> results(List<String> search) {
        return null;
    }

    List<String> users;

    @Override
    public List<SearchResult> looseResults(List<String> search) {
        if (users == null) updateUsers();

        List<SearchResult> results = new ArrayList<>();

        for (var user : users) {
            if (user.contains("comp2100")) {
                int n = 0;
            }

            var map = new HashMap<Character, Integer>();
            var charArray = user.toCharArray();
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

            //TODO: Cut username to same length as what is being searched
            // or search just the length that is currently inserted


            //TODO: Maybe something like Map<Char, Int, Int>, with the first int being number of occurences and second being number of matches
            //then count the number of matches vs word length


            var currentResult = new SearchResult(user, SearchToken.Query.User, Collections.singletonList(user));
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
            else sum -= 0.6;
        }


        for (var entry : map.keySet()) {
            var num = map.get(entry);
            sum -= num > 0 ? num * 0.3 : num;
        }

        return sum * (1+Math.abs(word.length() - length)*0.1);
    }



    public void updateUsers() {
        users = Firebase.getInstance().readAllUsernames();
        //TODO: observer?
    }
}
