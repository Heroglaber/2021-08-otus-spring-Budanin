package ru.otus.quiz;

import com.opencsv.exceptions.CsvException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, CsvException {
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        QuestionsReader questionsReader = context.getBean(QuestionsReader.class);
        List<Question> quizQuestions = questionsReader.getQuestions();
        Quiz quiz = context.getBean(Quiz.class);
        quiz.setQuestions(quizQuestions);
        quiz.start();
    }
}
