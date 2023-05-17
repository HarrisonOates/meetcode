package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;

import com.example.myeducationalapp.SyntaxHighlighting.DetectCodeBlock;

import org.junit.Test;

/**
 * @author u7468212 Harrison Oates
 */
public class SyntaxHighlightingParsingTest {
    @Test (timeout = 1000)
    public void wholeParagraphTest(){
        String s =
                "Hello world! I know what I want to do. \n" +
                        "Let's write code! \n" +
                        "```// This initializes a variable to zero \n " +
                        "int i = 0;```\n" +
                        "Here is some more code \n" +
                        "```/* I like for loops - they're super easy */ for(int i = 0; i < 5; i++){\n" +
                        "    sum += 1;\n" +
                        "}```";
        String expected = "Hello world! I know what I want to do. " +
                "<br>Let's write code! <br><font color = \"grey\">" +
                "//&nbsp;This&nbsp;initializes&nbsp;a&nbsp;variable&nbsp;to&nbsp;zero&nbsp;</font><br>&nbsp;<font color = \"#FF6000\">int</font>&nbsp;<font color = \"purple\">i</font>" +
                "&nbsp;<font color = \"black\">=</font>&nbsp;<font color = \"blue\">0</font><font color = \"black\">;</font>" +
                "<br>Here is some more code <br>" +
                "<font color = \"grey\">/*&nbsp;I&nbsp;like&nbsp;for&nbsp;loops&nbsp;-&nbsp;they're&nbsp;super&nbsp;easy&nbsp;*/</font>&nbsp;<font color = \"#FF6000\">for</font>" +
                "<font color = \"black\">(</font><font color = \"#FF6000\">int</font>&nbsp;<font color = \"purple\">i</font>&nbsp;<font color = \"black\">=</font>&nbsp;<font color = \"blue\">0</font><font color = \"black\">;" +
                "</font>&nbsp;<font color = \"purple\">i</font>&nbsp;<font color = \"black\">&lt;</font>&nbsp;<font color = \"blue\">5</font><font color = \"black\">;</font>&nbsp;<font color = \"purple\">i</font><font color = \"black\">+</font><font color = \"black\">+</font>" +
                "<font color = \"black\">)</font><font color = \"black\">{</font><br>&nbsp;&nbsp;&nbsp;&nbsp;<font color = \"purple\">sum</font>&nbsp;<font color = \"black\">+" +
                "</font><font color = \"black\">=</font>&nbsp;<font color = \"blue\">1</font><font color = \"black\">;</font><br><font color = \"black\">}</font>";

        String found = DetectCodeBlock.parseCodeBlocks(s);
        System.out.println(found);
        assertEquals(expected, found);
    }
}
