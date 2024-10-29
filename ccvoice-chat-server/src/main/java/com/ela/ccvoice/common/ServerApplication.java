package com.ela.ccvoice.common;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ela.ccvoice")
public class ServerApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ServerApplication.class, args);
    }
}
