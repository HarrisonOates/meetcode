package com.example.myeducationalapp.SyntaxHighlighting;

import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.IDENTIFIER;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.KEYWORD;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.NEWLINE;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.NUMERIC_LITERAL;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.PUNCTUATOR;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.STRING_LITERAL;
import static com.example.myeducationalapp.SyntaxHighlighting.Token.Type.WHITESPACE;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Takes a block of Java code and transforms it into an XML string.
 * Only one colour scheme for the moment.
 * @author Harrison Oates
 */
public class Parser {
    /**
     * Temporary colour scheme while developing is based on a mix of Android Studio
     * and Stack Overflow, for ease.
     * keywords are orange
     * numbers are blue
     * Chars and strings are green.
     * semicolons are orange, but rest of punctuation is black.
     * TODO - Types?
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

    // Recursively parses the given tokens.
    public void parseCodeBlock() {
        if (tokenizer.currentToken == null){
            return;
        }
        Token t = tokenizer.currentToken;
        if (t.getType() == KEYWORD){
            highlightedBlock.append("<font color = \"orange\">");
            highlightedBlock.append(t.getToken());
            highlightedBlock.append("</font>");
        }
        else if (t.getType() == NUMERIC_LITERAL){
            highlightedBlock.append("<font color = \"blue\">");
            highlightedBlock.append(t.getToken());
            highlightedBlock.append("</font>");
        }
        else if (t.getType() == STRING_LITERAL){
            highlightedBlock.append("<font color = \"green\">");
            highlightedBlock.append(t.getToken());
            highlightedBlock.append("</font>");
        }
        else if (t.getType() == IDENTIFIER){
            if (identifiers.contains(t.getToken())){
                highlightedBlock.append(t.getToken());
            }
            else{
                highlightedBlock.append("<font color = \"purple\">");
                identifiers.add(t.getToken());
                highlightedBlock.append(t.getToken());
                highlightedBlock.append("</font>");
            }


        }
        else if (t.getType() == WHITESPACE || t.getType() == NEWLINE){
            highlightedBlock.append(t.getToken());
        }
        else if (t.getType() == PUNCTUATOR){
            highlightedBlock.append("<font color = \"purple\">");
            highlightedBlock.append(t.getToken());
            highlightedBlock.append("</font>");
        }
        else {
            throw new IllegalTokenException("Token " + t.getToken() + " of type " + t.getType() + " is not recognized");
        }
        tokenizer.next();
        parseCodeBlock();

    }

    // Testing shows that the printing isn't working quite as intended.
    // Need to look over tokenizer some more
    public static void main(String[] args) throws FileNotFoundException {
        StringBuilder input = new StringBuilder();
        File testRead = new File("sample.txt");
        Scanner read = new Scanner(testRead);
        while (read.hasNextLine()){
            input.append(read.nextLine());
        }

        Tokenizer tok = new Tokenizer(input.toString());
        Parser parse = new Parser(tok);
        parse.parseCodeBlock();
        System.out.println(parse.highlightedBlock);

    }




}
