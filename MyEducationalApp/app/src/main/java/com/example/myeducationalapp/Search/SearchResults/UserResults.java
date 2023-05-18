package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Search.SearchParsing.SearchToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Obtains the results of how likely each user is to be the result being searched for
 * @author u7146309 Jayden Skidmore
 */
public class UserResults extends Results{
    /**
     * A list of all user names as a string
     */
    List<String> users;

    /**
     * Returns a list containing the results from a user search
     * @param search The user inputted search
     * @return A list containing results from each user
     */
    @Override
    public List<SearchResult> results(List<String> search) {
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

            var currentResult = new SearchResult(user, SearchToken.Query.User, Collections.singletonList(user));
            currentResult.setConfidence(score);
            results.add(currentResult);
        }


        return results;
    }


    /**
     * Calculates the relative score of a word, indicating how likely it is to match the search result
     * @param word A word from the search
     * @param map A map of all the characters from the current user, and the occurrence of each character
     * @param length The length of the user name
     * @return A relative score of how close a searched word is to the user name
     */
    double wordScore(String word, HashMap<Character, Integer> map, double length) {
        double sum = 0;

        for (var character : word.toCharArray()) {
            var c = Character.toLowerCase(character);
            if (map.containsKey(c)) map.put(c,map.get(c)-1);
            else sum -= 1.2; //Letters in search, not in user
        }


        for (var entry : map.keySet()) {
            var num = map.get(entry);
            sum -= num > 0 ? num * 0.1 : -num; //> 0 = in user, not in search. < 0 = letters more freq in search
        }

        return sum * (1+Math.abs(word.length() - length)*0.1);
    }


    /**
     * Updates the users separately, instead of updating every time something is searched
     */
    public void updateUsers() {
        users = Firebase.getInstance().readAllUsernames();
    }
}
