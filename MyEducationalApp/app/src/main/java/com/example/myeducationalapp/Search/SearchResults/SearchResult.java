package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Search.SearchToken;

public class SearchResult {
    private final int id;
    private final SearchToken.Query type;
    private final double confidence;

    public SearchResult(int id, SearchToken.Query type, double confidence) {
        this.id = id;
        this.type = type;
        this.confidence = confidence;
    }

    public int getId() {
        return id;
    }

    public SearchToken.Query getType() {
        return type;
    }

    public double getConfidence() {
        return confidence;
    }
}
