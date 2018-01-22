package com.jptech;

import com.jptech.service.MessageService;
import com.jptech.service.Service;

import java.io.BufferedReader;
import java.io.FileReader;

public class SalesService {

    public static void main(String[] args) {
	    // Message service
        Service messageService = new MessageService();

        try{
            System.out.println(System.getProperty("user.dir"));

            // Setup service
            messageService.setup();

            // Start service
            messageService.start();

            // File reader to get samples
            BufferedReader reader = new BufferedReader(new FileReader("samples.txt"));

            String data = "";

            // Reads samples
            while ((data = reader.readLine()) != null){
                messageService.onDataReceived(data);
            }

            reader.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
