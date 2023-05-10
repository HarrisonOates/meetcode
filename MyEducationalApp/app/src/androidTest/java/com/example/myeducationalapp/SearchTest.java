package com.example.myeducationalapp;

import static org.junit.Assert.assertTrue;

import com.example.myeducationalapp.Search.SearchResults.TopicResults;
import com.example.myeducationalapp.Search.SearchResults.UserResults;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchTest {
    @Test
    public void topicTest(){
        TopicResults search = new TopicResults();
        var results = search.looseResults(new ArrayList<String>(Arrays.asList("recursion", "datastructure", "algorithm")));
        var user = (new UserResults()).looseResults(new ArrayList<String>(Arrays.asList("test","t")));

//        System.out.println(Arrays.toString(user.toArray()));
//        System.out.println("Test");

        assertTrue(false);
    }
}
