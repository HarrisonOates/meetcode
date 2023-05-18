package com.example.myeducationalapp.Search.SearchParsing;

import java.util.List;

/**
 * A formattes expression of a statement expression
 * @author Jayden Skidmore
 */
public class StatementExp extends Exp{
    //Stores the words in the statement
    private final List<String> statement;

    /**
     * Constructs the expression
     * @param statement The statement
     */
    public StatementExp(List<String> statement) {
        this.statement = statement;
    }

    /**
     * Returns a string representation of the queries
     * @return string representation
     */
    @Override
    public String show() {
        return statement.toString();
    }

    /**
     * Returns a decomposition of the query
     * @return decomposed version of the queries
     */
    @Override
    public List<String> decomposition() {

        return statement;
    }

    @Override
    public List<Exp> expressions() {
        return null;
    }
}
