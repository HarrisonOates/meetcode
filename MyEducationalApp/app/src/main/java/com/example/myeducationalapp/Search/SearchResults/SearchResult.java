package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Search.SearchToken;

import java.util.List;

public class SearchResult {
    private final int id;
    private final SearchToken.Query type;
    private double confidence;
    private final List<String> words;

    public SearchResult(int id, SearchToken.Query type, List<String> words) {
        this.id = id;
        this.type = type;
        this.words = words;
    }

    public int getId() {
        return id;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public SearchToken.Query getType() {
        return type;
    }

    public double getConfidence() {
        return confidence;
    }

    public List<String> getWords() {
        return words;
    }
}
