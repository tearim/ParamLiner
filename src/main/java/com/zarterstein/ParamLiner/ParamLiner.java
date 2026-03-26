package com.zarterstein.ParamLiner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParamLiner is a lightweight, zero-dependency Java utility for parsing command-line style strings
 * into parameters, with intelligent handling of quoted strings. It supports various parsing modes
 * through configurable flags, making it suitable for simple command parsing, configuration files,
 * or any scenario requiring whitespace-separated tokens with quote preservation.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Handles quoted parameters (e.g., "param with spaces") as single tokens</li>
 *   <li>Configurable whitespace condensation and quote trimming</li>
 *   <li>Low memory footprint with no external dependencies</li>
 *   <li>Thread-safe for immutable instances (use setters for mutable configuration)</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * ParamLiner parser = new ParamLiner(ParamLiner.CONDENSE_ALL | ParamLiner.TRIM_ALL_ANSWERS);
 * String[] params = parser.parse("command param1 \"param2 with spaces\" param3");
 * // Result: ["command", "param1", "param2 with spaces", "param3"]
 * }</pre>
 *
 * @author Mark Gondelman (human-written code)
 * @version 1.0
 * @since 1.0
 */
public class ParamLiner {
    /**
     * Flag indicating no special processing (default behavior).
     */
    public static final int QUOTES_NOT_TRIMMED_NOT_CONDENSED = 0;

    /**
     * Flag to condense all whitespace in the input string before parsing.
     */
    public static final int CONDENSE_ALL = 1;

    /**
     * Flag to ignore quotes and treat all whitespace-separated tokens equally.
     */
    public static final int IGNORE_QUOTES = 2;

    /**
     * Flag to trim whitespace from all quoted parameters after extraction.
     */
    public static final int TRIM_ALL_ANSWERS = 4;

    private static final String QUOTE_UNIQUE_PREFIX = "PARSER_SECRET_";
    private boolean condenseAll = false;
    private boolean ignoreQuotes = false;
    private boolean trimAllQuotedParams = false;

    /**
     * Creates a ParamLiner instance with default settings (no flags).
     */
    public ParamLiner() {
    }

    /**
     * Creates a ParamLiner instance with the specified parsing flags.
     *
     * @param flags bitwise OR combination of parsing flags (e.g., CONDENSE_ALL | IGNORE_QUOTES)
     */
    public ParamLiner(int flags) {
        if ((flags & CONDENSE_ALL) != 0) condenseAll = true;
        if ((flags & IGNORE_QUOTES) != 0) ignoreQuotes = true;
        if ((flags & TRIM_ALL_ANSWERS) != 0) trimAllQuotedParams = true;
    }

    /**
     * Sets whether to condense all whitespace before parsing.
     *
     * @param newValue true to enable condensation, false otherwise
     * @return this instance for method chaining
     */
    public ParamLiner setCondenseAll(boolean newValue) {
        condenseAll = newValue;
        return this;
    }

    /**
     * Sets whether to ignore quotes during parsing.
     *
     * @param newValue true to ignore quotes, false otherwise
     * @return this instance for method chaining
     */
    public ParamLiner setIgnoreQuotes(boolean newValue) {
        ignoreQuotes = newValue;
        return this;
    }

    /**
     * Sets whether to trim whitespace from quoted parameters.
     *
     * @param newValue true to trim quoted parameters, false otherwise
     * @return this instance for method chaining
     */
    public ParamLiner setTrimAllQuotedParams(boolean newValue) {
        trimAllQuotedParams = newValue;
        return this;
    }

    /**
     * Generates a unique string placeholder that does not appear in the input string.
     * Used internally to temporarily replace quoted strings during parsing.
     *
     * @param param the input string to parse
     * @return a unique placeholder string
     */
    private String getUniqueStringReplacement(String param) {
        String nonContained = QUOTE_UNIQUE_PREFIX;
        while (param.contains(nonContained)) {
            nonContained = QUOTE_UNIQUE_PREFIX + UUID.randomUUID().toString() + '_';
        }
        return nonContained;
    }

    /**
     * Condenses multiple consecutive whitespace characters into single spaces.
     * This is typically called after quote processing unless CONDENSE_ALL is enabled.
     *
     * @param param the string to condense
     * @return the condensed string with normalized whitespace
     */
    private String condenseWhitespaces(String param) {
        return param.replaceAll("[\\s][\\s]+", " ");
    }

    /**
     * Parses the input string into an array of parameters, handling quoted strings as single tokens
     * unless IGNORE_QUOTES is set. The parsing behavior is controlled by the instance's flags.
     *
     * @param param the input string to parse (must not be null)
     * @return an array of parsed parameters
     * @throws IllegalArgumentException if param is null
     */
    public String[] parse(String param) {
        if (param == null) {
            throw new IllegalArgumentException("Input parameter cannot be null");
        }
        if (param.isEmpty()) {
            return new String[0];
        }
        if (condenseAll) {
            param = condenseWhitespaces(param);
        }
        if (ignoreQuotes) {
            return splitString(param);
        }
        Pattern pattern = Pattern.compile("(?s)(^|[\\s])(\".*?[\\S]+?.*?\")");
        Matcher matcher = pattern.matcher(param);

        String replacer = getUniqueStringReplacement(param);

        String[] matches = matcher.results().map(MatchResult::group).toArray(String[]::new);
        for (int i = 0; i < matches.length; i++) {
            param = param.replace(matches[i], ' ' + replacer + i);
        }
        if (!condenseAll) {
            param = condenseWhitespaces(param);
        }
        String[] words = param.trim().split("(?s)[\\s]+");
        List<String> workingWords = new ArrayList<>();

        for (int i = 0; i < words.length; i++) {
            if (words[i].contains(replacer)) {
                int index = Integer.parseInt(words[i].replaceAll(replacer + "(?s)([\\d]+).*", "$1"));
                String quotedVal = matches[index].trim().substring(1, matches[index].trim().length() - 1);
                quotedVal = trimAllQuotedParams ? quotedVal.trim() : quotedVal;
                workingWords.add(quotedVal);
                // Handle trailing text after quoted string (e.g., "param1 param2"param3)
                String trail = words[i].replace(replacer + index, "").trim();
                if (!trail.isEmpty()) {
                    workingWords.add(trail);
                }
            } else {
                workingWords.add(words[i]);
            }
        }
        return workingWords.toArray(new String[0]);
    }

    /**
     * Splits the input string by whitespace, ignoring quotes.
     *
     * @param param the string to split
     * @return an array of whitespace-separated tokens
     */
    private String[] splitString(String param) {
        return param.trim().split("(?s)[\\s]+");
    }

}
