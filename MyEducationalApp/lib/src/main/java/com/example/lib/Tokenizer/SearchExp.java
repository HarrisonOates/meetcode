package com.example.tokenizer;

import java.util.List;

public class SearchExp extends Exp{
    Exp expression;
    Exp nextExpression;

    public SearchExp(Exp expression) {
        this.expression = expression;
    }

    public SearchExp(Exp expression, Exp nextExpression) {
        this.expression = expression;
        this.nextExpression = nextExpression;
    }

    @Override
    public String show() {
        if (expression == null) return "";
        return expression.show() + (nextExpression != null ? " ; " + nextExpression.show() : "");
    }

    //TODO
    @Override
    public List<String> decomposition() {
        return null;
    }
}
