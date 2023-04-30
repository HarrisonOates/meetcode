package com.example.myeducationalapp.SyntaxHighlighting;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tokenizer class to convert a file of java code into tokens
 * At present, tokenizer does not check for correctness.
 * @author Harrison Oates
 */
public class Tokenizer {
    // General structure of tokenizer taken from lab 5.
    private String buffer;
    Token currentToken;

    /* The list of currently-used java language keywords
     * Taken from https://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html
     */
    private final Set<String> keywords = new HashSet<>(Arrays.asList(
              "abstract", "assert"
            , "boolean", "break", "byte"
            , "case", "catch", "char", "continue"
            , "default", "do"
            , "double", "else", "enum", "extends"
            , "final", "finally", "float", "for"
            , "if", "implements", "import", "instanceof", "int", "interface"
            , "long"
            , "native", "new"
            , "package", "private", "protected", "public"
            , "return"
            , "short", "static", "strictfp", "super", "synchronized"
            , "this", "throw", "throws", "transient", "try"
            , "void", "volatile"
            , "while"
    ));

    public Tokenizer(String text){
        buffer = text;
        next();
    }

    // TODO - refactor the while loops if possible
    public void next(){
        if (buffer.isEmpty()){
            currentToken = null;
            return;
        }

        char firstChar = buffer.charAt(0);
        // Whitespace
        if (Character.isWhitespace(firstChar)){
            StringBuilder s = new StringBuilder(" ");
            int pointer = 1;
            while (pointer < buffer.length() && Character.isWhitespace(buffer.charAt(pointer))){
                s.append(" ");
                pointer++;
            }
            currentToken = new Token(s.toString(), Token.Type.WHITESPACE);
        }
        // Newline
        else if (firstChar == '\n'){
            currentToken = new Token("\n", Token.Type.NEWLINE);
        }

        // Identifier / keywords
        else if (Character.isAlphabetic(firstChar)){
            StringBuilder s = new StringBuilder(firstChar);
            int pointer = 1;
            while (pointer < buffer.length() && Character.isAlphabetic(buffer.charAt(pointer))){
                s.append(buffer.charAt(pointer));
                pointer++;
            }

            // Check if it is in the identifier list.
            if (keywords.contains(s.toString())){
                currentToken = new Token(s.toString(), Token.Type.KEYWORD);
            }
            else{
                currentToken = new Token(s.toString(), Token.Type.IDENTIFIER);
            }
        }
        // Checking for numeric literal
        else if (Character.isDigit(firstChar)){
            StringBuilder s = new StringBuilder(firstChar);
            int pointer = 1;
            while (pointer < buffer.length() && (Character.isDigit(firstChar) || firstChar == '.')){
                s.append(buffer.charAt(pointer));
                pointer++;
            }
            currentToken = new Token(s.toString(), Token.Type.NUMERIC_LITERAL);
        }

        // Checking for string literal
        else if (firstChar == '\'' || firstChar == '\"'){
            StringBuilder s = new StringBuilder(firstChar);
            int pointer = 1;
            while (pointer < buffer.length() && buffer.charAt(pointer) != firstChar){
                s.append(buffer.charAt(pointer));
                pointer++;
            }

            currentToken = new Token(s.toString(), Token.Type.STRING_LITERAL);
        }

        // If none of the above are satisfied, then it's punctuation
        else {
            currentToken = new Token(String.valueOf(firstChar), Token.Type.PUNCTUATOR);
        }

        int tokenLen = currentToken.getToken().length();
        buffer = buffer.substring(tokenLen);

    }

}
