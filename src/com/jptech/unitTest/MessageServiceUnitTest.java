package com.jptech.unitTest;

import static org.junit.Assert.*;

import com.jptech.service.MessageService;
import org.junit.*;

import java.util.Random;

public class MessageServiceUnitTest {
    // Message service
    private static MessageService m_service;

    // Random products singular
    private static String[] m_products_singular = {
            "apple", "orange", "coke", "banana",
            "pineapple", "strawberry", "chocolate",
            "shoe", "boot", "hat", "shirt", "jacket",
            "wine", "beer", "pen", "tomato", "T shirt"
    };

    // Random products plural
    private static String[] m_products_plural = {
            "apples", "oranges", "cokes", "bananas",
            "pineapples", "strawberries", "chocolates",
            "shoes", "boots", "hats", "shirts", "jackets",
            "wines", "beers", "pens", "tomatoes", "T shirts"
    };

    // Random delta op
    private static String[] m_delta = { "add", "subtract", "multiply" };

    @BeforeClass
    public static void setup(){
        m_service = new MessageService();

        // Calls service setup
        m_service.setup();

        // Service start
        m_service.start();
    }

    // Print sales log after tests
    @After
    public void printLog(){
        System.out.println(m_service.getLogSales());
        System.out.println();
        System.out.println(m_service.getLogAdjustments());
    }

    // Random sale processing (first message pattern)
    @Test
    public void pattern1Test(){
        Random random = new Random();

        for(int i=0; i<100; i++){
            m_service.onDataReceived(m_products_singular[random.nextInt(m_products_singular.length)] + " at " +
                    String.valueOf(random.nextInt(60)) + "p");
        }
    }

    // Random sale processing (second message pattern)
    @Test
    public void pattern2Test(){
        Random random = new Random();

        for(int i=0; i<100; i++){
            m_service.onDataReceived(random.nextInt(300) + " sales of " + m_products_plural[random.nextInt(m_products_plural.length)] + " at " +
                    String.valueOf(random.nextInt(60)) + "p each");
        }
    }

    // Random sale processing (third message pattern)
    @Test
    public void pattern3Test(){
        Random random = new Random();

        for(int i=0; i<100; i++){
            m_service.onDataReceived(m_delta[random.nextInt(m_delta.length)] + " " + String.valueOf(random.nextInt(60)) + "p " +
                m_products_plural[random.nextInt(m_products_plural.length)]);
        }
    }
}
