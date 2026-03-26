package com.zarterstein.ParamLiner;

import static com.zarterstein.ParamLiner.ParamLiner.CONDENSE_ALL;
import static com.zarterstein.ParamLiner.ParamLiner.IGNORE_QUOTES;
import static com.zarterstein.ParamLiner.ParamLiner.TRIM_ALL_ANSWERS;

public class ParamLinerTestConsole {
    /**
     * Console test method for demonstration. Parses sample inputs and prints results.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        ParamLiner parser = new ParamLiner();

        System.out.println("=== Basic Parsing ===");
        String[] result1 = parser.parse("command param1 param2");
        System.out.println("Input: 'command param1 param2'");
        System.out.println("Output: " + java.util.Arrays.toString(result1));

        System.out.println("\n=== With Quotes ===");
        String[] result2 = parser.parse("command param1 \"param2 with spaces\" param3");
        System.out.println("Input: 'command param1 \"param2 with spaces\" param3'");
        System.out.println("Output: " + java.util.Arrays.toString(result2));

        System.out.println("\n=== With Flags (CONDENSE_ALL | TRIM_ALL_ANSWERS) ===");
        ParamLiner parserWithFlags = new ParamLiner(CONDENSE_ALL | TRIM_ALL_ANSWERS);
        String[] result3 = parserWithFlags.parse("command   param1   \"  param2 with spaces  \"   param3");
        System.out.println("Input: 'command   param1   \"  param2 with spaces  \"   param3'");
        System.out.println("Output: " + java.util.Arrays.toString(result3));

        System.out.println("\n=== Ignore Quotes ===");
        ParamLiner parserIgnore = new ParamLiner(IGNORE_QUOTES);
        String[] result4 = parserIgnore.parse("command param1 \"param2 with spaces\" param3");
        System.out.println("Input: 'command param1 \"param2 with spaces\" param3'");
        System.out.println("Output: " + java.util.Arrays.toString(result4));
    }
}
