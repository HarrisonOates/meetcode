package com.example.myeducationalapp.SyntaxHighlighting;

/**
 * Detects when a user has entered a code block by using three backticks in a row.
 * Will scan until three backticks are again entered, or the end of the string,
 * whichever is shorter.
 * @author Harrison Oates
 */

public class DetectCodeBlock {

    /**
     * @param s - the input string from the user
     * @return html to render in textView.
     */
    public static String parseCodeBlocks(String s){
        StringBuilder toReturn = new StringBuilder();

        // Pointers to create substrings
        int startOfBlock = 0;
        boolean openCodeBlock = false;
        int pointer = 0;
        int consecutiveCount = 0;

        while (pointer < s.length()){
            if (s.charAt(pointer) == '`'){
                consecutiveCount++;
                if (consecutiveCount == 3){

                    if (openCodeBlock) {
                        // This is our sign to end parsing
                        String toParse = s.substring(startOfBlock, pointer + 1);
                        Tokenizer t = new Tokenizer(toParse);
                        Parser p    = new Parser(t);
                        p.parseCodeBlock();
                        toReturn.append(p.getHighlightedBlock());
                        openCodeBlock = false;
                        pointer++;
                        startOfBlock = pointer;
                    }
                    else {
                        String notCode = s.substring(startOfBlock, pointer + 1);
                        toReturn.append(notCode);
                        openCodeBlock = true;
                        pointer++;
                        startOfBlock = pointer;
                    }
                }
            }
            else {
                consecutiveCount = 0;
                pointer++;
            }
        }
        // In the case where a code block has not been closed, we're still rendering the string
        if (openCodeBlock){
            String toParse = s.substring(startOfBlock);
            Tokenizer t = new Tokenizer(toParse);
            Parser p    = new Parser(t);
            p.parseCodeBlock();
            toReturn.append(p.getHighlightedBlock());
        }

        return toReturn.toString();

    }
}
