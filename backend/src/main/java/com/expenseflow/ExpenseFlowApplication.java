package com.expenseflow;

import com.expenseflow.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
public class ExpenseFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseFlowApplication.class, args);
    }
}
