package com.jptech.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InboundParser {
    // Pattern 1
    public final static String MESSAGE_PATTERN_1 = "(.+)\\s\\bat\\b\\s(\\d+)p";

    // Pattern 2
    public final static String MESSAGE_PATTERN_2 = "(\\d+)\\s\\bsales of\\b\\s(.+)\\s\\bat\\b\\s(\\d+)p\\s\\beach\\b";

    // Pattern 3
    public final static String MESSAGE_PATTERN_3 = "\\b(Add|Subtract|Multiply)\\b\\s(\\d+)p\\s(.+)";

    // Patterns / tasks map
    private Map<Pattern, PacketProcessor> m_patterns;

    public InboundParser() {
        m_patterns = new HashMap<>();
    }

    /**
     * Add packet processor to the patterns'map
     *
     * @param pattern
     * key pattern
     *
     * @param processor
     * processor to execute upon message of specified pattern's type is received
     */
    public void add(String pattern, PacketProcessor processor){
        m_patterns.put(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE), processor);
    }

    /**
     * Parse message data and calls the associated processor if the pattern matches.
     * Drop the message otherwise.
     *
     * @param message
     * message to be processed.
     */
    public boolean parse(String message){
        // Loop patterns / processors
        for(Map.Entry<Pattern, PacketProcessor> entry : m_patterns.entrySet()){
            // Matcher
            Matcher matcher = entry.getKey().matcher(message);

            if(matcher.matches()){
                if(entry.getValue() != null)
                    entry.getValue().read(matcher);

                return true;
            }
        }

        return false;
    }

    public interface PacketProcessor {
        /**
         * Must be implemented to consume regex results as per Message type.
         *
         * @param matcher
         * regex matcher results
         */
        void read(Matcher matcher);
    }
}
