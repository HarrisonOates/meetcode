package com.example.myeducationalapp.Search;

import static com.example.myeducationalapp.Search.SearchToken.Query.*;

import java.util.HashMap;

/**
 * Defines all general tokens used in the parsing the search system
 * @author Jayden Skidmore
 */
public class SearchToken {
    /**
     * Tokens for each type of search query
     */
    public enum Query {Question, User, Discussion, Topic, Separator, Word;

        /**
         * returns the char representation of a token, or null if its a work
         * @return char representation of a query
         */
        String getType() {
            for (var entry : tokens.entrySet()) {
                if (this.equals(entry.getValue())) return entry.getKey();
            }
            return null;
        }

        /**
         * Returns the token as a string
         * @return the string representation of a token
         */
        @Override
        public String toString() {
            switch (this) {
                case Question:
                    return "Question";
                case User:
                    return "User";
                case Discussion:
                    return "Discussion";
                case Topic:
                    return "Topic";
                case Separator:
                    return "Separator";
                case Word:
                    return "Word";
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    /**
     * A map storing all the token types and the char representation of each.
     * Used for easy lookup
     */
    static HashMap<String, Query> tokens = new HashMap<>()
    {{
        put("?", Question);
        put("@", User);
        put("!", Discussion);
        put("#", Topic);
        put(";", Separator);
    }};

    /**
     * Returns the corresponding query from a given string
     * @param token Current string to tokenize
     * @return the corresponding query, or null if not a query
     */
    public static Query get(String token) {
        Query query = tokens.get(token);
        if (query != null) return query;
        return Word;
    }


    /**
     * Allows for storing a token as a data structure
     */
    public String token;
    public Query query;

    /**
     * Constructor for a token
     * @param type The string being tokenized
     * @param query The type of token
     */
    public SearchToken(String type, Query query) {
        this.token = type;
        this.query = query;
    }

    /**
     * Returns the string which has been tokenized
     * @return underlying string of a token
     */
    public String getToken() {
        return token;
    }

    /**
     * Return the type of query the token represents
     * @return the query of the token
     */
    public Query getQuery() {
        return query;
    }
}
