package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;

import com.example.myeducationalapp.SyntaxHighlighting.Token;
import com.example.myeducationalapp.SyntaxHighlighting.Tokenizer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SyntaxHighlightingTest {

    @Parameterized.Parameters
    public static Collection<Object[]> tokenized() {
        String[] declaration         = new String[]{"int", "Token(int, KEYWORD)"};
        String[] numericLiteral      = new String[]{"1.23", "Token(1.23, NUMERIC_LITERAL)"};
        String[] newLine             = new String[]{"\n", "Token(\n, NEWLINE)"};
        String[] arbitraryWhitespace = new String[]{"  ", "Token(  , WHITESPACE)"};
        String[] punctuator          = new String[]{"{", "Token({, PUNCTUATOR)"};
        String[] stringLiteral       = new String[]{"\"Hello World!\"", "Token(\"Hello World!\", STRING_LITERAL)"};


        return Arrays.asList(new String[][]{declaration, numericLiteral,newLine, arbitraryWhitespace, punctuator,stringLiteral});
    }

    @Parameterized.Parameter(0)
    public String inputText;

    @Parameterized.Parameter(1)
    public String token;

    // Checks whether individual tokens are tokenizing as expected
    @Test(timeout = 500)
    public void SingleTokenTest(){
        Tokenizer tok = new Tokenizer(inputText);
        Token t = tok.getCurrentToken();
        assertEquals(token, t.toString());
    }

}
