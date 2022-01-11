package ru.otus.library.configs;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongock
@EnableMongoRepositories(basePackages = {"ru.otus.library.repositories"})
public class MongoConfig {
}
