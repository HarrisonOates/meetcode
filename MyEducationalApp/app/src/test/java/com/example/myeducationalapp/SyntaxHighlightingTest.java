package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;

import com.example.myeducationalapp.SyntaxHighlighting.DetectCodeBlock;
import com.example.myeducationalapp.SyntaxHighlighting.Token;
import com.example.myeducationalapp.SyntaxHighlighting.Tokenizer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author u7468212 Harrison Oates
 */

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
        String[] binaryPrefix        = new String[]{"0b1001", "Token(0b1001, NUMERIC_LITERAL)"};
        String[] hexPrefix          = new String[]{"0xDEADBEEF", "Token(0xDEADBEEF, NUMERIC_LITERAL)"};


        return Arrays.asList(new String[][]{declaration, numericLiteral, newLine, arbitraryWhitespace, punctuator, stringLiteral, binaryPrefix, hexPrefix});
    }

    @Parameterized.Parameter()
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

    @Test(timeout = 500)
    public void lineTokenTest(){
        String s = "int i = 0;\n";
        Tokenizer tok = new Tokenizer(s);
        List<String> results = new ArrayList<>();
        while(tok.getCurrentToken() != null){
            results.add(tok.getCurrentToken().toString());
            tok.next();
        }
        List<String> expected = Arrays.asList(
                "Token(int, KEYWORD)",
                "Token( , WHITESPACE)",
                "Token(i, IDENTIFIER)",
                "Token( , WHITESPACE)",
                "Token(=, PUNCTUATOR)",
                "Token( , WHITESPACE)",
                "Token(0, NUMERIC_LITERAL)",
                "Token(;, PUNCTUATOR)",
                "Token(\n, NEWLINE)");
        assertEquals(expected, results);
    }


}
