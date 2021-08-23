package ru.otus.quiz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import ru.otus.quiz.dao.QuestionDaoCSV;
import ru.otus.quiz.exceptions.QuestionsReaderException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Класс QuestionsReader")
@SpringBootTest
class QuestionsReaderTest {

    @Configuration
    static class TestConfiguration {
        //empty
    }

    @DisplayName("должен выкидывать QuestionsReaderException в случае, если в строке файла csv меньше 3 значений")
    @Test
    void shouldThrowExceptionIfNotEnoughValues() {
        Resource csvResource = new DefaultResourceLoader().getResource("classpath:questionsTest1.csv");

        Exception exception = assertThrows(QuestionsReaderException.class,
                ()->{
                    QuestionDaoCSV questionsReader = new QuestionDaoCSV(csvResource);
                    questionsReader.getAll();
                });
        assertEquals("Csv string must contain at least 3 values: question string, " +
        "at least 1 option and answer", exception.getMessage());
    }

    @DisplayName("должен выкидывать QuestionsReaderException в случае, если номер правильного ответа не соответствует количеству вариантов ответа")
    @Test
    void shouldThrowExceptionIfRightAnswerInvalid() {
        Resource csvResource = new DefaultResourceLoader().getResource("classpath:questionsTest2.csv");

        Exception exception = assertThrows(QuestionsReaderException.class,
                ()->{
                    QuestionDaoCSV questionsReader = new QuestionDaoCSV(csvResource);
                    questionsReader.getAll();
                });
        assertEquals("Csv string contains invalid number of options" +
                " or wrong right answer", exception.getMessage());
    }

    @DisplayName("должен выкидывать QuestionsReaderException в случае, если в строке вопроса - пустая строка")
    @Test
    void shouldThrowExceptionIfStringIsEmpty() {
        Resource csvResource = new DefaultResourceLoader().getResource("classpath:questionsTest3.csv");

        Exception exception = assertThrows(QuestionsReaderException.class,
                ()->{
                    QuestionDaoCSV questionsReader = new QuestionDaoCSV(csvResource);
                    questionsReader.getAll();
                });
        assertEquals("Csv file contains empty strings", exception.getMessage());
    }

    @DisplayName("должен выкидывать QuestionsReaderException в случае, если в строке значение null")
    @Test
    void shouldThrowExceptionIfStringIsNull() {
        Resource csvResource = new DefaultResourceLoader().getResource("classpath:questionsTest4.csv");

        Exception exception = assertThrows(QuestionsReaderException.class,
                ()->{
                    QuestionDaoCSV questionsReader = new QuestionDaoCSV(csvResource);
                    questionsReader.getAll();
                });
        assertEquals("Csv file contains null values", exception.getMessage());
    }

    @DisplayName("должен выкидывать NumberFormatException в случае, если значение правильного ответа - не число")
    @Test
    void shouldThrowExceptionIfRightAnswerIsNotNumeric() {
        Resource csvResource = new DefaultResourceLoader().getResource("classpath:questionsTest5.csv");

        Exception exception = assertThrows(NumberFormatException.class,
                ()->{
                    QuestionDaoCSV questionsReader = new QuestionDaoCSV(csvResource);
                    questionsReader.getAll();
                });
    }
}