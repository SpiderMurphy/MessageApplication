package com.jptech.models;

public class Delta {
    // Operation type
    private String m_operator;

    // Value
    private int m_value;

    public Delta(String operator, int value) {
        this.m_operator = operator;
        this.m_value = value;
    }

    public String get_operator() {
        return m_operator;
    }

    public void set_operator(String operator) {
        this.m_operator = operator;
    }

    public int get_value() {
        return m_value;
    }

    public void set_value(int value) {
        this.m_value = value;
    }
}
