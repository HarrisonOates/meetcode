package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Question;
import com.example.myeducationalapp.QuestionSet;
import com.example.myeducationalapp.Search.SearchParsing.SearchToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;

/**
 * Obtains the results of how likely each post is to be the result being searched for
 * @author u7146309
 */
public class PostResults extends Results{
    /**
     * A list of all possible search results, which is updated to include likelihood
     */
    private ArrayList<SearchResult> searchResults;

    /**
     * A map of all the question topics and their weighting of likelihood
     */
    HashMap<QuestionSet.Category, Double> topics = new HashMap<>(Map.of(QuestionSet.Category.Algorithm,1.0, QuestionSet.Category.ControlFlow,1.0, QuestionSet.Category.DataStructure,1.0, QuestionSet.Category.Miscellaneous,1.0, QuestionSet.Category.Recursion,1.0));

    /**
     * Returns a list containing the results from a post search
     * @param search The user inputted search
     * @return A list containing results from each post
     */
    @Override
    public List<SearchResult> results(List<String> search) {
        if (searchResults == null) updatePosts();

        searchResults.forEach(searchResult -> {
            double confidence = 0;
            double count = 0;
            for (var searchWord : search) {
                for (var questionWord : searchResult.getWords()) {
                    if (Objects.equals(searchWord.toLowerCase(), questionWord.toLowerCase())) {
                        count++;
                        break;
                    }
                    else if (questionWord.toLowerCase().contains(searchWord.toLowerCase())) {
                        count += (double)searchWord.length()/(double)questionWord.length();
                        break;
                    }

                }
            }

            double percentage = count / (double) search.size();
            confidence = -12 * Math.pow((percentage - 1),2.0);

            var multiplier = topics.get(QuestionSet.charToCategory(searchResult.getId().charAt(0)));

            searchResult.setConfidence(multiplier * confidence);
        });

        return searchResults;
    }


    /**
     * Updates the posts separately, instead of updating every time something is searched
     */
    public void updatePosts() {
        SortedMap<String, Question> questions = QuestionSet.getInstance().getUsedQuestionSets();

        if (questions == null) return;


        searchResults = new ArrayList<>();

        var entrySet = questions.keySet();
        for (String entry : entrySet) {
            if (!topics.containsKey(questions.get(entry).getCategory())) continue;


            var comments = (String)Firebase.getInstance().readQuestionComments(entry).await();
            if (comments == null) continue;
            var splitComments = comments.split("\n");
            int index = 0;

            for (var comment : splitComments) {
                var sections = comment.split("\t");
                String id = entry + "\n" + index;
                List<String> words = List.of(sections[1].split(" "));

                var searchResult = new SearchResult(id, SearchToken.Query.Discussion, words);
                searchResults.add(searchResult);
                index++;
            }
        }
    }

    /**
     * Sets the likelihood of each post for being the one searched for
     * @param topics A map of each post and it's likelihood
     */
    public void setTopics(HashMap<QuestionSet.Category, Double> topics) {
        this.topics = topics;
    }
}
