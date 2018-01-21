package com.jptech.models;;

public class Sale {
    // Product
    private Product m_product;

    // Value
    private int m_value;

    public Sale(){

    }

    public Sale(Product product, int value) {
        this.m_product = product;
        this.m_value = value;
    }

    public Product get_product() {
        return m_product;
    }

    public void set_product(Product product) {
        this.m_product = product;
    }

    public int get_value() {
        return m_value;
    }

    public void set_value(int value) {
        this.m_value = value;
    }
}
