package com.example.tokenizer;

import java.util.List;

public class StatementExp extends Exp{
    private List<String> statement;

    public StatementExp(List<String> statement) {
        this.statement = statement;
    }

    @Override
    public String show() {
        return statement.toString();
    }

    //TODO
    @Override
    public List<String> decomposition() {
        return null;
    }
}
