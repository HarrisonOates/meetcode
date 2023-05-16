package com.example.myeducationalapp.Search;

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

public class Search {
    private static Search instance;

    private static PostResults postResults;
    private static QuestionResults questionResults;
    private static TopicResults topicResults;
    private static UserResults userResults;

    private static SearchParser searchParser;

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

    public static Search getInstance() {
        if (instance == null) instance = new Search();
        return instance; //TODO
    }



    public ArrayList<SearchResult> search(String searchInput) {
        resetFilters();


        List<String> words = Arrays.asList(searchInput.split(" "));

        var parsedSearch = searchParser.parseSearch(searchInput);
        var expressions = parsedSearch.expressions();


        for (var exp : expressions) {
            if (exp instanceof QueryExp && ((QueryExp) exp).getQueryType() == SearchToken.Query.Topic) {
                var queryResults = topicResults.results(words);
                HashMap<Character, Double> topics = new HashMap<>();

                for (var topic : queryResults) {
                    if (topic.getConfidence() >= -2) {
                        var id = topic.getId().charAt(0);
                        Double confidence = 2 - Math.abs(topic.getConfidence()) / 2;
                        if (!topics.containsKey(id) || confidence > topics.get(id)) topics.put(id, confidence);
                    }
                }

                setFilters(topics);

                //TODO: cut off results at confidence < -2 (?)
                //TODO: bump up confidence of most confident search
                //TODO: store filter for use later
            }
        }


        ArrayList<SearchResult> searchResults = new ArrayList<>();


        //TODO: Modify weights if certain queries are used. E.g. reduce user if topic is searched for
        for (var exp : expressions) {
            if (exp instanceof QueryExp) {
                //TODO
                var expWords = exp.decomposition();

                switch (((QueryExp) exp).getQueryType()) {
                    case User: {
                        var results = userResults.results(expWords);
                        searchResults.addAll(results);
                        }
                    case Discussion: {
                        var results = postResults.results(expWords);
                        searchResults.addAll(results);
                    }
                    case Question: {
                        var results = questionResults.results(expWords);
                        searchResults.addAll(results);
                    }
                    case Topic: {
                        var results = topicResults.results(expWords);
                        searchResults.addAll(results);
                    }
                }
            }
            else if (exp instanceof StatementExp) {
                //TODO
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


        //TODO: Get results - Parse, separate queries, get results

        //TODO: Sort results




        //TODO: Only return top ____ results

        return searchResults;
    }


//    private ArrayList<SearchResult> sortResults(ArrayList<SearchResult> results) {
//        //TODO
//        results.sort(new SortResults());
//        return results;
//    }


    class SortResults implements Comparator<SearchResult> {

        @Override
        public int compare(SearchResult o1, SearchResult o2) {
            return (int) ((o1.getConfidence() - o2.getConfidence())*(-100));
        }
    }

    public void updateSearchData() {
        postResults.updatePosts();
        questionResults.updateQuestions();
        userResults.updateUsers();
    }



    private void setFilters(HashMap<Character, Double> topics) {
        postResults.setTopics(topics);
        questionResults.setTopics(topics);
    }

    private void resetFilters() {
        var noFilter = new HashMap<>(Map.of('0',1.0,'1',1.0,'2',1.0,'3',1.0,'4',1.0));
        postResults.setTopics(noFilter);
        questionResults.setTopics(noFilter);
    }


    //TODO: Sort results
    //TODO: Filter results
    //TODO: Interface to return results
    //TODO: Store search types/classes so they don't keep initialising
    //TODO: Updating search function data
}
