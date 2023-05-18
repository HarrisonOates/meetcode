package com.example.myeducationalapp.SyntaxHighlighting;

import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.IDENTIFIER;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.KEYWORD;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.MULTILINE_COMMENT;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.NEWLINE;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.NUMERIC_LITERAL;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.PUNCTUATOR;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.SINGLELINE_COMMENT;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.STRING_LITERAL;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.WHITESPACE;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Takes a block of Java code and transforms it into a HTML string.
 * Only one colour scheme for the moment.
 * @author u7468212 Harrison Oates
 */
public class Parser {
    /**
     * Our colourscheme:
     * - keywords are orange
     * - numbers are blue
     * - Chars and strings are green.
     * - All punctuation is black
     * - Classes are teal
     * - Identifiers are magenta
     * - Comments are grey.
     */

    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }

    Tokenizer tokenizer;
    // Notes if identifier has been used before.
    Set<String> identifiers = new HashSet<>();
    StringBuilder highlightedBlock;

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.highlightedBlock = new StringBuilder();
    }

    // Parses the given tokens.
    public void parseCodeBlock() {
        while(tokenizer.currentToken != null){
            Token t = tokenizer.currentToken;
            if (t.getType() == KEYWORD){
                highlightedBlock.append("<font color = \"#FF6000\">");
                highlightedBlock.append(t.getToken());
                highlightedBlock.append("</font>");
            }
            else if (t.getType() == NUMERIC_LITERAL){
                highlightedBlock.append("<font color = \"blue\">");
                highlightedBlock.append(t.getToken());
                highlightedBlock.append("</font>");
            }
            else if (t.getType() == STRING_LITERAL){
                highlightedBlock.append("<font color = \"#008000\">");
                highlightedBlock.append(t.getToken());
                highlightedBlock.append("</font>");
            }
            else if (t.getType() == IDENTIFIER){
                // A shortcut for classes
                if (Character.isUpperCase(t.getToken().charAt(0))){
                    highlightedBlock.append("<font color = \"#0080A0\">");
                    highlightedBlock.append(t.getToken());
                    highlightedBlock.append("</font>");
                }

                else {
                    highlightedBlock.append("<font color = \"purple\">");
                    identifiers.add(t.getToken());
                    highlightedBlock.append(t.getToken());
                    highlightedBlock.append("</font>");
                }


            }
            else if (t.getType() == NEWLINE){
                highlightedBlock.append("<br>");
            }
            else if (t.getType() == WHITESPACE){
                // For portability, we're going to have to translate to html character &nbsp;
                StringBuilder whitespace = new StringBuilder();

                for (int i = 0; i < t.getToken().length(); i++){
                    whitespace.append("&nbsp;");
                }


                highlightedBlock.append(whitespace);
            }
            else if (t.getType() == PUNCTUATOR){
                highlightedBlock.append("<font color = \"black\">");
                highlightedBlock.append(replaceProblematicCharacters(t.getToken().charAt(0)));
                highlightedBlock.append("</font>");
            }

            else if (t.getType() == SINGLELINE_COMMENT){
                highlightedBlock.append("<font color = \"grey\">");
                for (int i = 0; i < t.getToken().length(); i++){
                    highlightedBlock.append(replaceProblematicCharacters( t.getToken().charAt(i)));
                }
                highlightedBlock.append("</font>");
            }

            else if (t.getType() == MULTILINE_COMMENT){
                // Things get a bit more fun when we introduce spaces and newlines.
                highlightedBlock.append("<font color = \"grey\">");

                for (int i = 0; i < t.getToken().length(); i++){
                   highlightedBlock.append(replaceProblematicCharacters( t.getToken().charAt(i)));
                }

                highlightedBlock.append("</font>");

            }
            else {
                throw new IllegalTokenException("Token " + t.getToken() + " of type " + t.getType() + " is not recognized");
            }
            tokenizer.next();
        }
    }

    /**
     * Some tokens will have a menagerie of reserved html characters.
     * This method screens them out effectively.
     * @param toCompare - the character to replace
     * @return the corresponding HTML
     */
    public static StringBuilder replaceProblematicCharacters(char toCompare){
        StringBuilder s = new StringBuilder();
        if (toCompare == '\n'){
            s.append("<br>");
        }
        else if (toCompare == ' '){
            s.append("&nbsp;");
        }
        else if (toCompare == '<'){
            s.append("&lt;");
        }
        else if (toCompare == '>'){
            s.append("&gt;");
        }
        else{
            s.append(toCompare);
        }

        return s;
    }

    public StringBuilder getHighlightedBlock() {
        return highlightedBlock;
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Set<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Set<String> identifiers) {
        this.identifiers = identifiers;
    }

    public void setHighlightedBlock(StringBuilder highlightedBlock) {
        this.highlightedBlock = highlightedBlock;
    }
}
