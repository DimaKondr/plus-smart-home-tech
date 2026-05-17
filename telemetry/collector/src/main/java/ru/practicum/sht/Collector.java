package ru.practicum.sht;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Collector {

    public static void main(String[] args) {

        //SpringApplication.run(Collector.class, args);


        try {
            SpringApplication.run(Collector.class, args);
        } catch (Throwable t) {
            System.err.println("!!! CRITICAL SPRING BOOT STARTUP ERROR !!!");
            t.printStackTrace(System.err);
            throw t;
        }

    }

}