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
                        "```int i = 0;```\n" +
                        "Here is some more code \n" +
                        "```for(int i = 0; i < 5; i++){\n" +
                        "    sum += 1;\n" +
                        "}```";
        String expected = "Hello world! I know what I want to do. <br>Let's write code! <br><font color = \"orange\">int</font>&nbsp;<font color = \"purple\">i</font>&nbsp;<font color = \"purple\">=</font>&nbsp;<font color = \"blue\">0</font><font color = \"purple\">;</font><br>Here is some more code <br><font color = \"orange\">for</font><font color = \"purple\">(</font><font color = \"orange\">int</font>&nbsp;<font color = \"purple\">i</font>&nbsp;<font color = \"purple\">=</font>&nbsp;<font color = \"blue\">0</font><font color = \"purple\">;</font>&nbsp;i&nbsp;<font color = \"purple\">&lt;</font>&nbsp;<font color = \"blue\">5</font><font color = \"purple\">;</font>&nbsp;i<font color = \"purple\">+</font><font color = \"purple\">+</font><font color = \"purple\">)</font><font color = \"purple\">{</font><br>&nbsp;&nbsp;&nbsp;&nbsp;<font color = \"purple\">sum</font>&nbsp;<font color = \"purple\">+</font><font color = \"purple\">=</font>&nbsp;<font color = \"blue\">1</font><font color = \"purple\">;</font><br><font color = \"purple\">}</font>";

        String found = DetectCodeBlock.parseCodeBlocks(s);
        System.out.println(found);
        assertEquals(expected, found);
    }
}
