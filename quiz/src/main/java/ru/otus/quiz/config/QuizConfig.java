package ru.otus.quiz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
public class QuizConfig {

    @Value("${quiz.language}")
    private String language = "en-EN";

    @Value("${csv.config.basename}")
    private String csvConfigBasename;

    @Bean
    public Locale locale() {
        return Locale.forLanguageTag(language);
    }

    @Bean
    public Resource csvResource(Locale locale) {
        ResourceBundle csvBundle = ResourceBundle.getBundle(csvConfigBasename, locale);
        String filepath = csvBundle.getString("csv.filePath");
        return new DefaultResourceLoader().getResource(filepath);
    }
}
