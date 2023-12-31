package com.example.myeducationalapp.SyntaxHighlighting;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tokenizer class to convert a file of java code into tokens
 * At present, tokenizer does not check for correctness.
 * @author u7468212 Harrison Oates
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
            , "short", "static", "strictfp", "super", "switch", "synchronized"
            , "this", "throw", "throws", "transient", "try"
            , "void", "volatile"
            , "while"
    ));

    public Tokenizer(String text){
        buffer = text;
        next();
    }

    public void next(){
        if (buffer.isEmpty()){
            currentToken = null;
            return;
        }

        char firstChar = buffer.charAt(0);
        // Whitespace
        if (Character.isWhitespace(firstChar)){
            // Is it a newline?
            if (firstChar == '\n'){
                currentToken = new Token(String.valueOf(firstChar), Token.Type.NEWLINE);

            }
            else {
                StringBuilder s = new StringBuilder(" ");
                int pointer = 1;
                while (pointer < buffer.length() && buffer.charAt(pointer) == ' '){
                    s.append(" ");
                    pointer++;
                }
                currentToken = new Token(s.toString(), Token.Type.WHITESPACE);
            }
        }
        // Newline
        else if (firstChar == '\n'){
            currentToken = new Token("\n", Token.Type.NEWLINE);
        }

        // Identifier / keywords
        else if (Character.isAlphabetic(firstChar)){
            StringBuilder s = new StringBuilder();
            int pointer = 0;
            while (pointer < buffer.length() && Character.isLetterOrDigit(buffer.charAt(pointer))){
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
            StringBuilder s = new StringBuilder();
            int pointer = 0;
            while (pointer < buffer.length() && (Character.isDigit(buffer.charAt(pointer)) || buffer.charAt(pointer) == '.'
                    ||  buffer.charAt(pointer) == 'b' || buffer.charAt(pointer) == 'x'
                    || buffer.charAt(pointer) == 'A' || buffer.charAt(pointer) == 'B'
                    || buffer.charAt(pointer) == 'C' || buffer.charAt(pointer) == 'D'
                    || buffer.charAt(pointer) == 'E' || buffer.charAt(pointer) == 'F')){
                s.append(buffer.charAt(pointer));
                pointer++;
            }
            currentToken = new Token(s.toString(), Token.Type.NUMERIC_LITERAL);
        }

        // Checking for string literal
        else if (firstChar == '\'' || firstChar == '\"'){
            StringBuilder s = new StringBuilder();
            s.append(firstChar);
            int pointer = 1;
            while (pointer < buffer.length()){
                // signals the end of the string
                if (buffer.charAt(pointer) == firstChar){
                    s.append(buffer.charAt(pointer));
                    break;
                }
                s.append(buffer.charAt(pointer));
                pointer++;
            }

            currentToken = new Token(s.toString(), Token.Type.STRING_LITERAL);
        }

        else if (firstChar == '/'){
            char comment = buffer.charAt(1);
            // It is a single line comment
            if (comment == '/'){
                StringBuilder s = new StringBuilder();
                s.append(firstChar);
                int pointer = 1;
                while (pointer < buffer.length()){
                    if (buffer.charAt(pointer) == '\n'){
                        break;
                    }
                    s.append(buffer.charAt(pointer));
                    pointer++;
                }

                currentToken = new Token(s.toString(), Token.Type.SINGLELINE_COMMENT);
            }
            // It is a multi-line comment
            else if (comment == '*'){
                StringBuilder s = new StringBuilder();
                s.append(firstChar);
                int pointer = 1;
                boolean almostEndOfComment = false;
                while (pointer < buffer.length()){
                    if (almostEndOfComment && buffer.charAt(pointer) == '/'){
                        s.append(buffer.charAt(pointer));
                        break;
                    }
                    else almostEndOfComment = buffer.charAt(pointer) == '*';
                    s.append(buffer.charAt(pointer));
                    pointer++;
                }
                currentToken = new Token(s.toString(), Token.Type.MULTILINE_COMMENT);
            }
            else {
                currentToken = new Token(String.valueOf(firstChar), Token.Type.PUNCTUATOR);
            }


        }
        // If none of the above are satisfied, then it's punctuation
        else {
            currentToken = new Token(String.valueOf(firstChar), Token.Type.PUNCTUATOR);
        }

        int tokenLen = currentToken.getToken().length();
        buffer = buffer.substring(tokenLen);

    }

    public Token getCurrentToken() {
        return currentToken;
    }
}
