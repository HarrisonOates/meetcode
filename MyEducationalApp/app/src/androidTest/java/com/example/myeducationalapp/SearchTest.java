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
import com.example.myeducationalapp.Search.SearchResults.TopicResults;
import com.example.myeducationalapp.Search.SearchResults.UserResults;

import org.junit.Assert;
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


    @Test
    public void parserTests() {
        SearchParser parser = new SearchParser();

        Exp parsedExp = parser.parseSearch("");
        assertNull(parsedExp);

        parsedExp = parser.parseSearch("test");
        assertTrue(parsedExp.expressions().get(0) instanceof StatementExp);
        assertEquals(parsedExp.expressions().get(0).decomposition().get(0), "test");




        parsedExp = parser.parseSearch("!discussion; statement; @user");
        Exp exp1 = parsedExp.expressions().get(0);
        assertTrue(exp1 instanceof QueryExp);
        assertEquals(exp1.decomposition().get(0), "discussion");
        assertEquals(((QueryExp) exp1).getQueryType(), SearchToken.Query.Discussion);

        Exp exp2 = parsedExp.expressions().get(1);
        assertTrue(exp2 instanceof StatementExp);
        assertEquals(exp2.decomposition().get(0), "statement");

        Exp exp3 = parsedExp.expressions().get(2);
        assertTrue(exp3 instanceof QueryExp);
        assertEquals(exp3.decomposition().get(0), "user");
        assertEquals(((QueryExp) exp3).getQueryType(), SearchToken.Query.User);



        parsedExp = parser.parseSearch("#this is a test");
        Exp exp = parsedExp.expressions().get(0);
        assertTrue(exp instanceof QueryExp);
        assertEquals(exp.decomposition().get(0), "this");
        assertEquals(exp.decomposition().get(1), "is");
        assertEquals(exp.decomposition().get(2), "a");
        assertEquals(exp.decomposition().get(3), "test");
        assertEquals(((QueryExp) exp).getQueryType(), SearchToken.Query.Topic);
    }



    @Test
    public void tokenizerTests() {
        SearchTokenizer tokenizer = new SearchTokenizer();
        assertTrue(tokenizer.getBuffer() == null && tokenizer.hasNext() && tokenizer.currentToken() == null);

        tokenizer = new SearchTokenizer("text");
        assertEquals(tokenizer.currentToken().getQuery(),SearchToken.Query.Word);

        tokenizer.newSearch("@text");
        assertEquals(tokenizer.currentToken().getQuery(),SearchToken.Query.User);


        tokenizer.newSearch("#text");
        assertEquals(tokenizer.currentToken().getQuery(),SearchToken.Query.Topic);


        tokenizer.newSearch("?text");
        assertEquals(tokenizer.currentToken().getQuery(),SearchToken.Query.Question);


        tokenizer.newSearch("!text");
        assertEquals(tokenizer.currentToken().getQuery(),SearchToken.Query.Discussion);

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

        tokenizer.newSearch("");
        assertNull(tokenizer.currentToken());
    }
}
