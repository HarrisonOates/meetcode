package com.example.myeducationalapp.SyntaxHighlighting;

import java.util.Objects;

/**
 * A class to save extracted tokens
 * @author Harrison Oates
 */
public class Token {
    // Defines the various types of tokens, as discussed in the lecture
    public enum Type {
        KEYWORD,        // See https://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html
        PUNCTUATOR,     // e.g {}(),;[]
        IDENTIFIER,     // e.g SquareRoot
        STRING_LITERAL, // e.g "String Literal" or 's'
        NUMERIC_LITERAL,
        WHITESPACE,      // As we're only highlighting syntax, we want to preserve whitespace somehow
        NEWLINE          // To preserve the structure.
    }

    private final String token;
    private final Type type;

    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Token)){
            return false;
        }
        return this.type == ((Token) o).getType() && this.token.equals(((Token) o).getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, type);
    }

    @Override
    public String toString() {
        return "Token(" +
                token + ", " +
                type +
                ')';
    }
}
