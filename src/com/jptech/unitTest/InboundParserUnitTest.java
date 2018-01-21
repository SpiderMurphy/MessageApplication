package com.jptech.unitTest;

import static org.junit.Assert.*;

import com.jptech.parser.InboundParser;
import org.atteo.evo.inflector.English;
import org.junit.*;


import java.util.regex.Matcher;

public class InboundParserUnitTest {
    // Parser
    private static InboundParser m_parser;

    @BeforeClass
    public static void setup(){
        m_parser = new InboundParser();

        // Add patterns
        // Message Pattern 1 processor "apple at 20p" -> "xxxxxx at yp",
        // there's also a brief test of Inflector library used to pluralize.
        m_parser.add(InboundParser.MESSAGE_PATTERN_1,
                (Matcher matcher) -> {
                    System.out.println(
                            "Product : " + English.plural(matcher.group(1)) + "\n" +
                            "value : " + matcher.group(2)
                    );
                });

        // Message Pattern 2 "xxx sales of zzzz at yp each".
        m_parser.add(InboundParser.MESSAGE_PATTERN_2,
                (Matcher matcher) -> {
                    System.out.println(
                            "Product : " + matcher.group(2) + "\n" +
                            "value : " + matcher.group(3) + "\n" +
                            "qty : " + matcher.group(1)
                    );
                });

        // Message Pattern 3 "Add/Subtract/Multiply xp zzzzzz"
        m_parser.add(InboundParser.MESSAGE_PATTERN_3,
                (Matcher matcher) -> {
                    System.out.println(
                            "Product : " + matcher.group(3) + "\n" +
                            "operator : " + matcher.group(1) + "\n" +
                            "value : " + matcher.group(2)
                    );
                });
    }

    @Test
    public void testMessagePattern1(){
        System.out.println();
        m_parser.parse("apple at 20p");
        m_parser.parse("tomato at 40p");
    }

    @Test
    public void testMessagePattern2(){
        System.out.println();
        m_parser.parse("300 sales of apples at 30p each");
    }

    @Test
    public void testMessagePattern3(){
        System.out.println();
        m_parser.parse("Add 20p Apples");
        m_parser.parse("Subtract 20p T shirts");
        m_parser.parse("Multiply 10p Osaka Meats");
    }
}
