package ru.practicum.sht;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Collector {

    public static void main(String[] args) {

        //SpringApplication.run(Collector.class, args);
        System.out.println(">>>>>>>>>>!!! JVM MAIN STARTED !!!<<<<<<<<<<");

        try {
            SpringApplication.run(Collector.class, args);
        } catch (Throwable t) {
            System.err.println(">>>>>>>>>>!!! SPRING LAUNCH FAILED !!!<<<<<<<<<<");
            t.printStackTrace(System.err);
            //throw t;
            System.exit(1);
        }

    }

}