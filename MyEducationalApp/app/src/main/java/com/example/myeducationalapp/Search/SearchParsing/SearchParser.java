package com.example.myeducationalapp.Search.SearchParsing;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class for parsing search results, splitting them up by their query and by
 * the individual words within them
 * @author u7146309 Jayden Skidmore
 */
public class SearchParser {
    /**
     * The tokenizer used to parse expressions
     */
    static SearchTokenizer tokenizer;

    /**
     * Initializer for the search parser
     */
    public SearchParser() {
        tokenizer = new SearchTokenizer();
    }

    /**
     * Parses a given string
     * @param searchQuery The search query to parse
     * @return The parsed version of the search
     */
    public SearchExp parseSearch(String searchQuery) {
        tokenizer.newSearch(searchQuery);
        return parseExp();
    }

    /**
     * Parses an expression into a formatted expression structure
     * Expressions follow the following grammar
     * <exp>    ::= <query> | <query> ; <exp> | <statement>
     *
     * @return a formatted expression
     */
    public static SearchExp parseExp() {
        // If nothing more to parse, return nothing
        if (!tokenizer.hasNext()) return null;

        // Ignores blank queries (The first thing is a query separator)
        if (tokenizer.currentToken().query == SearchToken.Query.Separator) {
            tokenizer.next();
            return parseExp();
        }

        // Parses statements
        else if (tokenizer.currentToken().query == SearchToken.Query.Word) {
            StatementExp statement = parseStatement(false);

            // If a separator is used after a statement, parse the next query
            if (tokenizer.hasNext()) {
                SearchExp nextQuery = parseExp();
                return new SearchExp(statement, nextQuery);
            }
            // Otherwise return the statement
            else return new SearchExp(statement);
        }

        // Parses queries
        else {
            QueryExp query = parseQuery();

            // If a separator is used after the query, parse the next query
            if (tokenizer.hasNext()) {
                SearchExp nextQuery = parseExp();
                return new SearchExp(query, nextQuery);
            }
            // Otherwise return the statement
            else return new SearchExp(query);
        }
    }

    /**
     * Parses a query into a formatted expression
     * Queries follow the below grammar
     * <query>   ::=  {specifier} <statement>
     *
     * @return a formatted query
     */
    public static QueryExp parseQuery() {
        //Separates the type of query from the statement
        SearchToken.Query queryType = tokenizer.currentToken().getQuery();

        //Parses the statement following a specifier
        tokenizer.next();
        StatementExp statement = parseStatement(true);

        //Returns the formatted query
        return new QueryExp(queryType, statement);
    }

    /**
     * Parses a statement into a formatted expression
     * <statement> ::= {word} | {word} <statement>
     *
     * @return a formatted statement
     */
    public static StatementExp parseStatement(boolean inQuery) {
        //Stores all words in an array
        ArrayList<String> statement = new ArrayList<>();

        //Splits words until it reaches a separator
        while (tokenizer.hasNext()) {
            // Checks if the token is a separator
            if (tokenizer.currentToken().query == SearchToken.Query.Separator) {
                tokenizer.next();

                //If there is nothing after the separator, ignore it
                if (!tokenizer.hasNext()) break;

                // If the separator is followed by a query (or currently in a query and followed by
                //      a regular statement), end the statement
                //      Note: This means ; can be used in a regular statement and ;; may
                //      be used to separate statements which are not queries
                if (inQuery || tokenizer.currentToken().query != SearchToken.Query.Word) {
                    return new StatementExp(statement);
                }

                //Add back the ';' if it is not intended to be a separator
                statement.add(";");
            }

            // Adds the parsed word to the statement list
            statement.add(tokenizer.currentToken().getToken());
            tokenizer.next();
        }

        // Returns the formatted statement
        return new StatementExp(statement);
    }

}
