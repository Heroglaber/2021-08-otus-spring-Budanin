package ru.otus.quiz;

import com.opencsv.exceptions.CsvException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.List;

@ComponentScan
@PropertySource("classpath:foo.properties")
public class Main {

    public static void main(String[] args) throws IOException, CsvException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        QuestionsReader questionsReader = context.getBean(QuestionsReader.class);
        List<Question> quizQuestions = questionsReader.getQuestions();
        Quiz quiz = context.getBean(Quiz.class);
        quiz.setQuestions(quizQuestions);
        quiz.start();
    }
}
