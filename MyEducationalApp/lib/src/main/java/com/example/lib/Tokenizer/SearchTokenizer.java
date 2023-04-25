package com.example.tokenizer;

import java.util.Objects;

public class SearchTokenizer {
    private String buffer;
    private SearchToken currentToken;

    public SearchTokenizer(String text) {
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }

    public SearchTokenizer(){
        buffer = null;
        currentToken = null;
    }


    public void newSearch(String input) {
        buffer = input;
        currentToken = null;
        next();
    }





    public void next() {
        //buffer = buffer.trim();     // remove whitespace

        if (Objects.equals(buffer, "")) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }
        if (buffer.charAt(0) == ' ') buffer = buffer.substring(1);


        char firstChar = buffer.charAt(0);

        SearchToken.Query query = SearchToken.get(firstChar + "");
        if (query != SearchToken.Query.Word) currentToken = new SearchToken(firstChar+"", query);
        else {
            int i = 0;
            StringBuilder word = new StringBuilder();

            while (buffer.length() > i && buffer.charAt(i) != ' ') {
                if (buffer.charAt(i) == ';') break;
                word.append(buffer.charAt(i));
                i++;
            }

            currentToken = new SearchToken(word.toString(), query);
        }


        // Remove the extracted token from buffer
        int tokenLen = currentToken.getToken().length();
        buffer = buffer.substring(tokenLen);
    }

    public SearchToken currentToken() {return currentToken;}

    public boolean hasNext() {return currentToken != null;}

    public String getBuffer() {return buffer;}



}
