package com.jptech.models;

import java.util.Objects;

/**
 * Product's model
 */
public class Product {
    // Product label
    private String m_label;

    public Product(String label) {
        this.m_label = label;
    }

    public String get_label() {
        return m_label;
    }

    public void set_label(String label) {
        this.m_label = label;
    }
}
