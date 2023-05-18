package com.example.myeducationalapp.Search.SearchParsing;

/**
 * Class for tokenizing search strings based upon different queries
 * @author u7146309 Jayden Skidmore
 */
public class SearchTokenizer {
    //Stores the untokenized section of the search string
    private String buffer;

    //Stores the current token which was just tokenized
    private SearchToken currentToken;

    /**
     * Initializes the tokenizer, if an input text is already obtained
     * @param text search to be tokenized
     */
    public SearchTokenizer(String text) {
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }

    /**
     * Initializes the tokenizer, if there is nothing initially to tokenize
     */
    public SearchTokenizer(){
        buffer = null;
        currentToken = null;
    }

    /**
     * Resets the tokenizer if a new search is wanting to be tokenized
     * @param input a search to be tokenized
     */
    public void newSearch(String input) {
        buffer = input;         //Saves the input text
        currentToken = null;    //Clears the current token
        next();                 //Extracts the first token
    }


    /**
     * Extracts the next token from the buffer and saves it to the current token
     */
    public void next() {
        //If the buffer is empty, theres nothing more to tokenize
        if (buffer.equals("")) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }

        //Remove white space
        while (buffer.charAt(0) == ' ') {
            buffer = buffer.substring(1);

            //If the buffer is then empty, end the tokenizer
            if (buffer.equals("")) {
                currentToken = null;    // if there's no string left, set currentToken null and return
                return;
            }
        }

        // extracts the first character
        char firstChar = buffer.charAt(0);

        //Checks whether the character represents a query
        SearchToken.Query query = SearchToken.get(firstChar + "");

        // If it does (token is not a word), return a query token
        if (query != SearchToken.Query.Word) currentToken = new SearchToken(firstChar+"", query);
        //If it does not, return the word as a token
        else {
            int i = 0;
            StringBuilder word = new StringBuilder();

            //Create a word, one character at a time. Words end with either whitespace, a ';' or end of the buffer
            while (buffer.length() > i && buffer.charAt(i) != ' ') {
                if (buffer.charAt(i) == ';') break;
                word.append(buffer.charAt(i));
                i++;
            }

            //Returns the word as a token
            currentToken = new SearchToken(word.toString(), query);
        }


        // Remove the extracted token from buffer
        int tokenLen = currentToken.getToken().length();
        buffer = buffer.substring(tokenLen);
    }

    /**
     * Returns the current token
     * @return current token
     */
    public SearchToken currentToken() {return currentToken;}

    /**
     * Returns whether there is more tokens to process
     * @return boolean - if there are more tokens
     */
    public boolean hasNext() {return currentToken != null;}

    /**
     * Returns the buffer
     * @return the current buffer
     */
    public String getBuffer() {return buffer;}



}
