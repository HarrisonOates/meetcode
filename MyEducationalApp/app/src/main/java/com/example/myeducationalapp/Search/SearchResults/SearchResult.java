package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Search.SearchParsing.SearchToken;

import java.util.List;

/**
 * @author u7146309 Jayden Skidmore
 */
public class SearchResult {
    private final String id;
    private final SearchToken.Query type;
    private double confidence;
    private final List<String> words;

    public SearchResult(String id, SearchToken.Query type, List<String> words) {
        this.id = id;
        this.type = type;
        this.words = words;
    }

    public String getId() {
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

    public String getStringResult() {return String.join(" ", words);}
}
