package com.example.tokenizer;

import java.util.HashMap;

import static com.example.tokenizer.SearchToken.Query.*;

public class SearchToken {
    public enum Query {Question, User, Discussion, Topic, Separator, Word;
    String getType() {
        for (var entry : tokens.entrySet()) {
            if (this.equals(entry.getValue())) return entry.getKey();
        }
        return null;
    }

    @Override
    public String toString() {
        return switch (this) {
            case Question   -> "Question";
            case User       -> "User";
            case Discussion -> "Discussion";
            case Topic      -> "Topic";
            case Separator  -> ";";
            case Word       -> " ";
        };
    }
    }

    static HashMap<String, Query> tokens = new HashMap<>()
    {{
        put("?", Question);
        put("@", User);
        put("!", Discussion);
        put("#", Topic);
        put(";", Separator);
    }};

    public static Query get(String token) {
        Query query = tokens.get(token);
        if (query != null) return query;
        return Word;
    }



    public String token;
    public Query query;

    public SearchToken(String type, Query query) {
        this.token = type;
        this.query = query;
    }

    public String getToken() {
        return token;
    }

    public Query getQuery() {
        return query;
    }
}
