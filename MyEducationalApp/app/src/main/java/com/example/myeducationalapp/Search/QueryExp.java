package com.example.myeducationalapp.Search;

import java.util.List;

/**
 * Represents a formatted expression of a query
 * @author Jayden Skidmore
 */
public class QueryExp extends Exp{
    //Stores the type of query
    private final SearchToken.Query queryType;
    //Stores the query itself
    private final StatementExp statement;

    /**
     * Constructs the expression
     * @param queryType The type of query
     * @param statement The statement within the query
     */
    public QueryExp(SearchToken.Query queryType, StatementExp statement) {
        this.queryType = queryType;
        this.statement = statement;
    }

    /**
     * Returns a string representation of the query
     * @return string representation
     */
    @Override
    public String show() {
        return queryType.getType() + "(" + queryType.toString() + ") " + statement.show();
    }

    /**
     * Returns a decomposition of the query
     * @return decomposed version of the queries
     */
    //TODO
    @Override
    public List<String> decomposition() {
        return null;
    }
}
