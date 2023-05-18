package com.example.myeducationalapp;

import static org.junit.Assert.*;

import com.example.myeducationalapp.Search.Search;
import com.example.myeducationalapp.Search.SearchParsing.Exp;
import com.example.myeducationalapp.Search.SearchParsing.QueryExp;
import com.example.myeducationalapp.Search.SearchParsing.SearchParser;
import com.example.myeducationalapp.Search.SearchParsing.SearchToken;
import com.example.myeducationalapp.Search.SearchParsing.SearchTokenizer;
import com.example.myeducationalapp.Search.SearchParsing.StatementExp;
import com.example.myeducationalapp.Search.SearchResults.PostResults;
import com.example.myeducationalapp.Search.SearchResults.QuestionResults;
import com.example.myeducationalapp.Search.SearchResults.TopicResults;
import com.example.myeducationalapp.Search.SearchResults.UserResults;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * @author u7146309 Jayden Skidmore
 */
public class SearchTest {
    /**
     * Test parser class
     */
    @Test
    public void parserTests() {
        //initialize parser
        SearchParser parser = new SearchParser();

        //test blank search
        Exp parsedExp = parser.parseSearch("");
        assertNull(parsedExp);

        //test parsing basic string
        parsedExp = parser.parseSearch("test");
        assertTrue(parsedExp.expressions().get(0) instanceof StatementExp);
        assertEquals(parsedExp.expressions().get(0).decomposition().get(0), "test");



        //Parsing multiple queries
        parsedExp = parser.parseSearch("!discussion; statement; @user");
        //Test first query
        Exp exp1 = parsedExp.expressions().get(0);
        assertTrue(exp1 instanceof QueryExp);
        assertEquals(exp1.decomposition().get(0), "discussion");
        assertEquals(((QueryExp) exp1).getQueryType(), SearchToken.Query.Discussion);

        //Test second query
        Exp exp2 = parsedExp.expressions().get(1);
        assertTrue(exp2 instanceof StatementExp);
        assertEquals(exp2.decomposition().get(0), "statement");

        //Test third query
        Exp exp3 = parsedExp.expressions().get(2);
        assertTrue(exp3 instanceof QueryExp);
        assertEquals(exp3.decomposition().get(0), "user");
        assertEquals(((QueryExp) exp3).getQueryType(), SearchToken.Query.User);

        //Test multiple words in query
        parsedExp = parser.parseSearch("#this is a test");
        Exp exp = parsedExp.expressions().get(0);
        assertTrue(exp instanceof QueryExp);
        assertEquals(exp.decomposition().get(0), "this");
        assertEquals(exp.decomposition().get(1), "is");
        assertEquals(exp.decomposition().get(2), "a");
        assertEquals(exp.decomposition().get(3), "test");
        assertEquals(((QueryExp) exp).getQueryType(), SearchToken.Query.Topic);
    }


    /**
     * Testing tokenizer class
     */
    @Test
    public void tokenizerTests() {
        //Test blank tokenizer
        SearchTokenizer tokenizer = new SearchTokenizer();
        assertTrue(tokenizer.getBuffer() == null && tokenizer.hasNext() && tokenizer.currentToken() == null);

        //Test word token
        tokenizer = new SearchTokenizer("text");
        assertEquals(tokenizer.currentToken().getQuery(),SearchToken.Query.Word);

        //Test user tokens
        tokenizer.newSearch("@text");
        assertEquals(tokenizer.currentToken().getQuery(),SearchToken.Query.User);

        //test topic tokens
        tokenizer.newSearch("#text");
        assertEquals(tokenizer.currentToken().getQuery(),SearchToken.Query.Topic);

        //test question tokens
        tokenizer.newSearch("?text");
        assertEquals(tokenizer.currentToken().getQuery(),SearchToken.Query.Question);

        //test discussion tokens
        tokenizer.newSearch("!text");
        assertEquals(tokenizer.currentToken().getQuery(),SearchToken.Query.Discussion);

        //Test multiple tokens together
        tokenizer.newSearch("!this is a; #test");
        assertEquals(tokenizer.currentToken().getQuery(), SearchToken.Query.Discussion);
        tokenizer.next();
        assertEquals(tokenizer.currentToken().getQuery(), SearchToken.Query.Word);
        assertEquals(tokenizer.currentToken().getToken(), "this");
        tokenizer.next();
        assertEquals(tokenizer.currentToken().getQuery(), SearchToken.Query.Word);
        assertEquals(tokenizer.currentToken().getToken(), "is");
        tokenizer.next();
        assertEquals(tokenizer.currentToken().getQuery(), SearchToken.Query.Word);
        assertEquals(tokenizer.currentToken().getToken(), "a");
        tokenizer.next();
        assertEquals(tokenizer.currentToken().getQuery(), SearchToken.Query.Topic);
        tokenizer.next();
        assertEquals(tokenizer.currentToken().getQuery(), SearchToken.Query.Word);
        assertEquals(tokenizer.currentToken().getToken(), "test");

        //Test blank token
        tokenizer.newSearch("");
        assertNull(tokenizer.currentToken());
    }



    /**
     * test searching in its entirety
     */
    @Test
    public void searchTest() {
        //Create search class
        Search search = new Search();
        search.updateSearchData();

        //Test program can handle blank inputs
        search.search("");

        //Test user search
        var results = search.search("@User");
        boolean allTrue = true;

        for (var result : results) {
            allTrue = allTrue && result.getType() == SearchToken.Query.User;
        }


        //test searching for something generally
        results = search.search("geun");
        for (var result : results) {
            if (Objects.equals(result.getWords().get(0), "geun")) assertEquals(result.getConfidence(), 0.0, 0.01);
        }


        //test discussion search
        results = search.search("!Discussion");
        allTrue = true;

        for (var result : results) {
            allTrue = allTrue && result.getType() == SearchToken.Query.Discussion;
        }

        //test topic search
        results = search.search("#topic");
        allTrue = true;

        for (var result : results) {
            allTrue = allTrue && result.getType() == SearchToken.Query.Topic;
        }

        //test question search
        results = search.search("?Question");
        allTrue = true;

        for (var result : results) {
            allTrue = allTrue && result.getType() == SearchToken.Query.Question;
        }
    }


    /**
     * Test topic search
     */
    @Test
    public void topicTest(){
        //Test exact search
        TopicResults topic = new TopicResults();
        var results = topic.results(Collections.singletonList("Algorithm"));

        for (var result : results) {
            if (result.getWords().equals("Algorithm")) assertEquals(result.getConfidence(),0, 0.0000001);
            else assertTrue(result.getConfidence() < -1);
        }



        //test general search
        results = topic.results(Collections.singletonList("Algo"));
        double notAlgorithm = Double.MIN_VALUE;
        double algorithm = Double.MIN_VALUE;

        for (var result : results) {
            if (result.getWords().get(0).equals("Algorithm")) if (result.getConfidence() > algorithm) algorithm = result.getConfidence();
            else if (result.getConfidence() > notAlgorithm) notAlgorithm = result.getConfidence();
        }

        assertTrue(algorithm > notAlgorithm);
    }

    /**
     * Test user search
     */
    @Test
    public void userTest() {
        //Test exact search
        UserResults user = new UserResults();
        var results = user.results(Collections.singletonList("jayden"));

        for (var result : results) {
            if (result.getWords().equals("jayden")) assertEquals(result.getConfidence(),0, 0.0000001);
            else assertTrue(result.getConfidence() < -0.3);
        }



        //Test general search
        results = user.results(Collections.singletonList("jay"));
        for (var result : results) {
            if (result.getWords().get(0).equals("jayden")) assertTrue(result.getConfidence() < -2);
        }
    }


    /**
     * Question search
     */
    @Test
    public void questionTest() {
        //test exact search
        QuestionResults question = new QuestionResults();
        var results = question.results(Collections.singletonList("Lists and recursion"));

        for (var result : results) {
            if (result.getWords().equals("Lists and recursion")) assertEquals(result.getConfidence(),0, 0.0000001);
            else assertTrue(result.getConfidence() < -0.3);
        }



        //test general search
        results = question.results(Collections.singletonList("Lists"));
        for (var result : results) {
            if (result.getWords().get(0).equals("Lists and recursion")) assertTrue(result.getConfidence() < -2);
        }
    }
}
