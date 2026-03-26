package com.zarterstein.ParamLiner;

import org.testng.annotations.Test;
import org.testng.Assert.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for ParamLiner class.
 * These tests cover various parsing scenarios, flag combinations, and edge cases.
 * Note: Javadocs and tests generated with LLM assistance.
 */
public class ParamLinerTest {

    @Test
    public void testBasicParsing() {
        ParamLiner parser = new ParamLiner();
        String[] result = parser.parse("command param1 param2");
        assertArrayEquals(new String[]{"command", "param1", "param2"}, result);
    }

    @Test
    public void testQuotedParameters() {
        ParamLiner parser = new ParamLiner();
        String[] result = parser.parse("command param1 \"param2 with spaces\" param3");
        assertArrayEquals(new String[]{"command", "param1", "param2 with spaces", "param3"}, result);
    }

    @Test
    public void testCondenseAllFlag() {
        ParamLiner parser = new ParamLiner(ParamLiner.CONDENSE_ALL);
        String[] result = parser.parse("command   param1   param2");
        assertArrayEquals(new String[]{"command", "param1", "param2"}, result);
    }

    @Test
    public void testIgnoreQuotesFlag() {
        ParamLiner parser = new ParamLiner(ParamLiner.IGNORE_QUOTES);
        String[] result = parser.parse("command param1 \"param2 with spaces\" param3");
        assertArrayEquals(new String[]{"command", "param1", "\"param2", "with", "spaces\"", "param3"}, result);
    }

    @Test
    public void testTrimQuotedParamsFlag() {
        ParamLiner parser = new ParamLiner(ParamLiner.TRIM_ALL_ANSWERS);
        String[] result = parser.parse("command \"  param2 with spaces  \" param3");
        assertArrayEquals(new String[]{"command", "param2 with spaces", "param3"}, result);
    }

    @Test
    public void testMultipleFlags() {
        ParamLiner parser = new ParamLiner(ParamLiner.CONDENSE_ALL | ParamLiner.TRIM_ALL_ANSWERS);
        String[] result = parser.parse("command   \"  param2 with spaces  \"   param3");
        assertArrayEquals(new String[]{"command", "param2 with spaces", "param3"}, result);
    }

    @Test
    public void testEmptyInput() {
        ParamLiner parser = new ParamLiner();
        String[] result = parser.parse("");
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testNullInput() {
        ParamLiner parser = new ParamLiner();
        assertThrows(IllegalArgumentException.class, () -> parser.parse(null));
    }

    @Test
    public void testTrailingQuotedString() {
        ParamLiner parser = new ParamLiner();
        String[] result = parser.parse("command \"param1 param2\"param3");
        assertArrayEquals(new String[]{"command", "param1 param2", "param3"}, result);
    }

    @Test
    public void testSetterMethods() {
        ParamLiner parser = new ParamLiner().setCondenseAll(true).setIgnoreQuotes(false).setTrimAllQuotedParams(true);
        String[] result = parser.parse("command   \"  param2  \"   param3");
        assertArrayEquals(new String[]{"command", "param2", "param3"}, result);
    }
}