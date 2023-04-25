package com.example.tokenizer;

import java.util.ArrayList;
import java.util.Scanner;

public class SearchParser {
    /**
     * The following exception should be thrown if the parse is faced with series of tokens that do not
     * correlate with any possible production rule.
     */
    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }

    // The tokenizer (class field) this parser will use.
    static SearchTokenizer tokenizer;

    public SearchParser() {
        tokenizer = new SearchTokenizer();
    }

    //TODO
    public SearchExp parseSearch(String searchQuery) {
        tokenizer.newSearch(searchQuery);
        return parseExp();
    }


    public static void main(String[] args) {
        // Create a scanner to get the user's input.
        Scanner scanner = new Scanner(System.in);

        /*
         Continue to get the user's input until they exit.
         To exit press: Control + D or providing the string 'q'
         */
        System.out.println("Provide a search string to be parsed:");
        while (scanner.hasNext()) {
            String input = scanner.nextLine();

            // Check if 'quit' is provided.
            if (input.equals("q"))
                break;

            // Create an instance of the tokenizer.
            tokenizer = new SearchTokenizer(input);
            tokenizer.newSearch(input);

            // Print out the expression from the parser.
            Exp expression = parseExp();
            if (expression == null) System.out.println("Null Expression");
            else System.out.println("Parsing: " + expression.show());
            //System.out.println("Evaluation: " + expression.decomposition().toString());
        }
    }

    /**
     * Adheres to the grammar rule:
     * <exp>    ::= <query> | <query> ; <exp> | <statement>
     *
     * @return type: Exp.
     */
    public static SearchExp parseExp() {
        if (!tokenizer.hasNext()) return null;


        if (tokenizer.currentToken().query == SearchToken.Query.Separator) {
            tokenizer.next();
            return parseExp();
        }


        else if (tokenizer.currentToken().query == SearchToken.Query.Word) {
            StatementExp statement = parseStatement(false);
            if (tokenizer.hasNext()) {
                SearchExp nextQuery = parseExp();
                return new SearchExp(statement, nextQuery);
            }
            return new SearchExp(statement);
        }


        else {
            QueryExp query = parseQuery();
            if (tokenizer.hasNext()) {
                SearchExp nextQuery = parseExp();
                return new SearchExp(query, nextQuery);
            }
            else return new SearchExp(query);
        }
    }

    /**
     * Adheres to the grammar rule:
     * <query>   ::=  {specifier} <statement>
     *
     * @return type: Exp.
     */
    public static QueryExp parseQuery() {
        SearchToken.Query queryType = tokenizer.currentToken().getQuery();
        tokenizer.next();

        StatementExp statement = parseStatement(true);

        return new QueryExp(queryType, statement);
    }

    /**
     * Adheres to the grammar rule:
     * <statement> ::= {word} | {word} <statement>
     *
     * @return type: Exp.
     */
    public static StatementExp parseStatement(boolean inQuery) {
        ArrayList<String> statement = new ArrayList<>();

        while (tokenizer.hasNext()) {
            if (tokenizer.currentToken().query == SearchToken.Query.Separator) {
                tokenizer.next();
                if (!tokenizer.hasNext()) break;
                if (inQuery || tokenizer.currentToken().query != SearchToken.Query.Word) {
                    return new StatementExp(statement);
                }
                statement.add(";");
            }

            statement.add(tokenizer.currentToken().getToken());
            tokenizer.next();
        }

        return new StatementExp(statement);
    }

}
