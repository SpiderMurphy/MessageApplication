package com.jptech.service;

import com.jptech.models.Delta;
import com.jptech.models.Product;
import com.jptech.models.Sale;
import com.jptech.parser.InboundParser;
import org.atteo.evo.inflector.English;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class MessageService implements Service {
    // Parser
    private InboundParser m_parser;

    // Sales map
    private Map<String, List<Sale>> m_sales;

    // Adjustments map
    private Map<String, List<Delta>> m_adjustments;

    // Plural to singular map
    private Map<String, String> m_names;

    // Running flag
    private boolean m_running;

    // Messages count
    private int m_counter;

    public MessageService() {
        this.m_parser = new InboundParser();

        this.m_sales = new HashMap<>();
        this.m_adjustments = new HashMap<>();
        this.m_names = new HashMap<>();
    }

    /**
     * Get log of sales total
     *
     * @return
     * log string
     */
    public String getLogSales(){
        String log = "";

        for(Map.Entry<String, List<Sale>> recordSet : m_sales.entrySet()){
            // Total
            int total = 0;

            // Sale type
            log += recordSet.getKey();

            // Sum amount
            for(Sale sale : recordSet.getValue()){
                total += sale.get_value();
            }

            log += " amount " + String.valueOf(total) + "p\n";
        }

        return log;
    }

    /**
     * Get log of adjustments rows
     *
     * @return
     * log string
     */
    public String getLogAdjustments(){
        String log = "";

        for(Map.Entry<String, List<Delta>> recordSet : m_adjustments.entrySet()){
            // Writes delta log
            for(Delta delta : recordSet.getValue()){
              log += recordSet.getKey() + " " + delta.get_operator() + " " + String.valueOf(delta.get_value()) + "p\n";
            }
        }

        return log;
    }

    @Override
    public void start() {
        m_running = true;
    }

    @Override
    public void stop() {
        m_running = false;
    }

    @Override
    public void setup() {
        /**
         * Setups parser patterns for incoming messages
         */

        // Message type 1
        m_parser.add(
                InboundParser.MESSAGE_PATTERN_1,
                (Matcher matcher) -> {
                    // Product
                    String productId = matcher.group(1);

                    // Plural
                    String productPlural = English.plural(productId);

                    // Sales list of the given type
                    List<Sale> saleList = null;

                    // Doesn't contains product, lets add it
                    if(!m_sales.containsKey(productPlural)) {
                        saleList = new ArrayList<>();
                        m_sales.put(productPlural, saleList);
                    }
                    else
                        saleList = m_sales.get(productPlural);

                    saleList.add(new Sale(
                        new Product(productId),
                        Integer.parseInt(matcher.group(2))
                    ));

                    m_names.put(productPlural, productId);
                });

        // Message type 2
        m_parser.add(
                InboundParser.MESSAGE_PATTERN_2,
                (Matcher matcher) -> {
                    // Tries to find a pre stored singular
                    String productId = m_names.get(matcher.group(2));

                    // Plural
                    String productPlural = matcher.group(2);

                    // Num sales
                    int numSales = Integer.parseInt(matcher.group(1));

                    // Sales list of the given type
                    List<Sale> saleList = null;

                    // Doesn't contains product, lets add it
                    if(!m_sales.containsKey(productPlural)) {
                        saleList = new ArrayList<>();
                        m_sales.put(productPlural, saleList);
                    }
                    else
                        saleList = m_sales.get(productPlural);

                    // Add n sales
                    for(int i=0; i<numSales; i++){
                        saleList.add(new Sale(
                                new Product(productId),
                                Integer.parseInt(matcher.group(3))
                        ));
                    }

                    if(!m_names.containsKey(productPlural))
                        m_names.put(productPlural, productId);
                });

        // Message type 3
        m_parser.add(
                InboundParser.MESSAGE_PATTERN_3,
                (Matcher matcher) -> {
                    // Operator
                    String operator = matcher.group(1);

                    // Plural
                    String productPlural = matcher.group(3);

                    // Value
                    int value = Integer.parseInt(matcher.group(2));

                    // If the sale never been recorded I wont add the adjustment
                    if(m_sales.containsKey(productPlural)) {
                        // Delta adjustment list
                        List<Delta> deltaList = null;

                        if (!m_adjustments.containsKey(productPlural)) {
                            deltaList = new ArrayList<>();
                            m_adjustments.put(productPlural, deltaList);
                        } else
                            deltaList = m_adjustments.get(productPlural);

                        Delta delta = new Delta(operator, value);

                        // Applies delta adjustment to sales
                        List<Sale> saleList = m_sales.get(productPlural);

                        for(Sale sale : saleList){
                            // Add
                            if(operator.equals("Add"))
                                sale.set_value(sale.get_value() + value);
                            // Subtract
                            else if(operator.equals("Subtract"))
                                sale.set_value(sale.get_value() - value);
                            // Multitply
                            else if(operator.equals("Multiply"))
                                sale.set_value(sale.get_value() * value);
                        }

                        deltaList.add(delta);
                    }


                });
    }

    @Override
    public void onDataReceived(String data) {
        if(m_running) {
            if(m_parser.parse(data)){
                // Increments message counter
                m_counter++;

                // Check if 50 reached
                if(m_counter == 50){
                    System.out.println("Service paused, no more messages accepted.");
                    System.out.println("\nSales adjustment report : \n\n" + getLogAdjustments());

                    // Stop service
                    stop();
                }
                // Check if counter is multiple of 10 and log sales'report
                else if((m_counter % 10) == 0){
                    System.out.println("\n\n" + getLogSales());
                }
            }
        }
    }
}
