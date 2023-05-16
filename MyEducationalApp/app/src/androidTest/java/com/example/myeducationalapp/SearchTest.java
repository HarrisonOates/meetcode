package com.example.myeducationalapp;

import static org.junit.Assert.assertTrue;

import com.example.myeducationalapp.Search.Search;
import com.example.myeducationalapp.Search.SearchResults.PostResults;
import com.example.myeducationalapp.Search.SearchResults.TopicResults;
import com.example.myeducationalapp.Search.SearchResults.UserResults;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author u7146309 Jayden Skidmore
 */
public class SearchTest {
    @Test
    public void topicTest(){
        var result = Search.getInstance().search("test");

        assertTrue(false);
    }
}
