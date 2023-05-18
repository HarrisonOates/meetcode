package com.example.myeducationalapp.Search;

import com.example.myeducationalapp.Question.QuestionSet;
import com.example.myeducationalapp.Search.SearchParsing.Exp;
import com.example.myeducationalapp.Search.SearchParsing.QueryExp;
import com.example.myeducationalapp.Search.SearchParsing.SearchParser;
import com.example.myeducationalapp.Search.SearchParsing.SearchToken;
import com.example.myeducationalapp.Search.SearchParsing.StatementExp;
import com.example.myeducationalapp.Search.SearchResults.PostResults;
import com.example.myeducationalapp.Search.SearchResults.QuestionResults;
import com.example.myeducationalapp.Search.SearchResults.SearchResult;
import com.example.myeducationalapp.Search.SearchResults.TopicResults;
import com.example.myeducationalapp.Search.SearchResults.UserResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Search class for interfacing all search classes
 * @author u7146309 Jayden Skidmore
 */
public class Search {
    /**
     * Singleton structure
     */
    private static Search instance;

    /**
     * Searches posts
     */
    private static PostResults postResults;
    /**
     * Searches questions
     */
    private static QuestionResults questionResults;
    /**
     * Searches topics
     */
    private static TopicResults topicResults;
    /**
     * Searches users
     */
    private static UserResults userResults;

    /**
     * Parses search strings
     */
    private static SearchParser searchParser;

    /**
     * Initialises search class bu updating all search types
     */
    public Search() {
        postResults = new PostResults();
        postResults.updatePosts();

        questionResults = new QuestionResults();
        questionResults.updateQuestions();

        topicResults = new TopicResults();

        userResults = new UserResults();
        userResults.updateUsers();

        searchParser = new SearchParser();

    }

    /**
     * Returns search instance
     * @return current search instance, or a new one if not present
     */
    public static Search getInstance() {
        if (instance == null) instance = new Search();
        return instance;
    }


    /**
     * Searches using an inputted string
     * @param searchInput search input from user
     * @return a list of search results, sorted in order of likelihood
     */
    public ArrayList<SearchResult> search(String searchInput) {
        resetFilters();


        List<String> words = Arrays.asList(searchInput.split(" "));

        var parsedSearch = searchParser.parseSearch(searchInput);
        var expressions = parsedSearch==null?new ArrayList<Exp>() : parsedSearch.expressions();


        for (var exp : expressions) {
            if (exp instanceof QueryExp && ((QueryExp) exp).getQueryType() == SearchToken.Query.Topic) {
                var queryResults = topicResults.results(words);
                HashMap<QuestionSet.Category, Double> topics = new HashMap<>();

                for (var topic : queryResults) {
                    if (topic.getConfidence() >= -2) {
                        var id = QuestionSet.charToCategory(topic.getId().charAt(0));
                        Double confidence = 2 - Math.abs(topic.getConfidence()) / 2;
                        if (!topics.containsKey(id) || confidence > topics.get(id)) topics.put(id, confidence);
                    }
                }

                setFilters(topics);
            }
        }

        ArrayList<SearchResult> searchResults = new ArrayList<>();

        for (var exp : expressions) {
            if (exp instanceof QueryExp) {
                var expWords = exp.decomposition();

                switch (((QueryExp) exp).getQueryType()) {
                    case User: {
                        var results = userResults.results(expWords);
                        searchResults.addAll(results);
                        break;
                        }
                    case Discussion: {
                        var results = postResults.results(expWords);
                        searchResults.addAll(results);
                        break;
                    }
                    case Question: {
                        var results = questionResults.results(expWords);
                        searchResults.addAll(results);
                        break;
                    }
                    case Topic: {
                        var results = topicResults.results(expWords);
                        searchResults.addAll(results);
                        break;
                    }
                }
            }
            else if (exp instanceof StatementExp) {
                var expWords = exp.decomposition();

                var users = userResults.results(expWords);
                var posts = postResults.results(expWords);
                var questions = questionResults.results(expWords);
                var topics = topicResults.results(expWords);

                searchResults.addAll(users);
                searchResults.addAll(posts);
                searchResults.addAll(questions);
                searchResults.addAll(topics);
            }
        }

        searchResults.sort(new SortResults());

        return searchResults;
    }


    /**
     * Comparator for sorting search results
     */
    class SortResults implements Comparator<SearchResult> {
        @Override
        public int compare(SearchResult o1, SearchResult o2) {
            return (int) ((o1.getConfidence() - o2.getConfidence())*(-100));
        }
    }

    /**
     * Updates all data from search types
     */
    public void updateSearchData() {
        postResults.updatePosts();
        questionResults.updateQuestions();
        userResults.updateUsers();
    }


    /**
     * Sets the filters for searching different topics
     * @param topics the topics and their assigned weighting
     */
    private void setFilters(HashMap<QuestionSet.Category, Double> topics) {
        postResults.setTopics(topics);
        questionResults.setTopics(topics);
    }

    /**
     * Resets all filters to default values
     */
    private void resetFilters() {
        HashMap<QuestionSet.Category, Double> noFilter = new HashMap<>(Map.of(QuestionSet.Category.Algorithm,1.0, QuestionSet.Category.ControlFlow,1.0, QuestionSet.Category.DataStructure,1.0, QuestionSet.Category.Miscellaneous,1.0, QuestionSet.Category.Recursion,1.0));
        postResults.setTopics(noFilter);
        questionResults.setTopics(noFilter);
    }
}
