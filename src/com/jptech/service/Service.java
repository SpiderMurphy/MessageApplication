package com.jptech.service;

public interface Service {
    /**
     * Starts the service
     */
    void start();

    /**
     * Stop the service
     */
    void stop();

    /**
     * Setup / config
     */
    void setup();

    /**
     * Processes incoming data
     *
     * @param data
     * data to be processed
     */
    void onDataReceived(String data);
}
