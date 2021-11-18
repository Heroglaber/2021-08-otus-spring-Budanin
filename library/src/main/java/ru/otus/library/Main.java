package ru.otus.library;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongock
@EnableMongoRepositories
@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Main.class);
    }
}
