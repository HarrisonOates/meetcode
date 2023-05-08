package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;

import com.example.myeducationalapp.SyntaxHighlighting.DetectCodeBlock;

import org.junit.Test;

public class SyntaxHighlightingParsingTest {
    @Test(timeout = 50000)
    public void wholeParagraphTest(){
        String s =
                "Hello world! I know what I want to do. \n" +
                        "Let's write code! \n" +
                        "```int i = 0;```\n" +
                        "Here is some more code" +
                        "```for(int i = 0; i < 5; i++){\n" +
                        "    sum += 1\n" +
                        "}```";
        String expected = "placeholder";

        String found = DetectCodeBlock.parseCodeBlocks(s);
        System.out.println(found);
        assertEquals(expected, found);
    }
}
