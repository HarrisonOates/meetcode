package com.example.myeducationalapp.Search.SearchParsing;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a formatted expression of a search
 * @author Jayden Skidmore
 */
public class SearchExp extends Exp{
    //Contains the first query expression
    private final Exp expression;
    //Contains a second query expression if present
    private Exp nextExpression;

    /**
     * Constructs the expression if only one query is present
     * @param expression Query expression
     */
    public SearchExp(Exp expression) {
        this.expression = expression;
    }

    /**
     * Constructs the expression if two queries are present
     * @param expression Query expression
     * @param nextExpression Subsequent query expressions
     */
    public SearchExp(Exp expression, Exp nextExpression) {
        this.expression = expression;
        this.nextExpression = nextExpression;
    }

    /**
     * Returns a string representation of the queries
     * @return string representation
     */
    @Override
    public String show() {
        if (expression == null) return "";
        return expression.show() + (nextExpression != null ? " ; " + nextExpression.show() : "");
    }

    /**
     * Returns a decomposition of the query
     * @return decomposed version of the queries
     */
    @Override
    public List<String> decomposition() {
        return null;
    }


    public List<Exp> expressions() {
        var list = new ArrayList<Exp>();

        list.add(expression);
        if (nextExpression != null) list.addAll(nextExpression.expressions());;
        return list;
    }
}
