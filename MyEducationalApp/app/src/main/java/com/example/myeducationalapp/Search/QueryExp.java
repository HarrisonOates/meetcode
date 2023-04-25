package com.example.tokenizer;

import java.util.List;

public class QueryExp extends Exp{
    private SearchToken.Query queryType;
    private StatementExp statement;

    public QueryExp(SearchToken.Query queryType, StatementExp statement) {
        this.queryType = queryType;
        this.statement = statement;
    }

    @Override
    public String show() {
        return queryType.getType() + "(" + queryType.toString() + ") " + statement.show();
    }

    //TODO
    @Override
    public List<String> decomposition() {
        return null;
    }
}
