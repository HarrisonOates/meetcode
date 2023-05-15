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
import java.util.List;

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
        List<String> words = Arrays.asList(searchInput.split(" "));

        var parsedSearch = searchParser.parseSearch(searchInput);
        var expressions = parsedSearch.expressions();

        ArrayList<SearchResult> searchResults = new ArrayList<>();



        for (var exp : expressions) {
            if (exp instanceof QueryExp && ((QueryExp) exp).getQueryType() == SearchToken.Query.Topic) {
                var queryResults = topicResults.looseResults(words);

                //TODO: cut off results at confidence < -2 (?)
                //TODO: bump up confidence of most confident search
                //TODO: store filter for use later
            }
        }

        for (var exp : expressions) {
            if (exp instanceof QueryExp) {
                //TODO
            }
            else if (exp instanceof StatementExp) {
                //TODO
            }
        }

        //TODO: Get results - Parse, separate queries, get results

        //TODO: Sort results

        return null;
    }


    private ArrayList<String> sortResults(ArrayList<String> results) {
        return null;
    }

    public void updateSearchData() {
        postResults.updatePosts();
        questionResults.updateQuestions();
        userResults.updateUsers();
    }


    //TODO: Sort results
    //TODO: Filter results
    //TODO: Interface to return results
    //TODO: Store search types/classes so they don't keep initialising
    //TODO: Updating search function data
}
