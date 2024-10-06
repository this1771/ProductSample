package com.example.productsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ProductSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductSampleApplication.class, args);
    }

}
