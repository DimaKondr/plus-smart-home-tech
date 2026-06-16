package ru.practicum.sht;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@ConfigurationPropertiesScan
@EnableFeignClients
public class ShoppingStoreApp {

    public static void main(String[] args) {

        SpringApplication.run(ShoppingStoreApp.class, args);

    }

}