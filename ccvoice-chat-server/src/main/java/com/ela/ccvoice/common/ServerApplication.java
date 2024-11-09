package com.ela.ccvoice.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ela.ccvoice")
@MapperScan({"com.ela.ccvoice.common.**.mapper"})
public class ServerApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ServerApplication.class, args);
    }
}
