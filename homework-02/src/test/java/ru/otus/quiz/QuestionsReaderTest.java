package ru.otus.quiz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.quiz.exceptions.QuestionsReaderException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Класс QuestionsReader")
class QuestionsReaderTest {

    @DisplayName("должен выкидывать QuestionsReaderException в случае, если в строке файла csv меньше 3 значений")
    @Test
    void shouldThrowExceptionIfNotEnoughValues() {

        Exception exception = assertThrows(QuestionsReaderException.class,
                ()->{
                    QuestionsReader questionsReader = new QuestionsReader("/questionsTest1.csv");
                });
        assertEquals("Csv string must contain at least 3 values: question string, " +
        "at least 1 option and answer", exception.getMessage());
    }

    @DisplayName("должен выкидывать QuestionsReaderException в случае, если номер правильного ответа не соответствует количеству вариантов ответа")
    @Test
    void shouldThrowExceptionIfRightAnswerInvalid() {

        Exception exception = assertThrows(QuestionsReaderException.class,
                ()->{
                    QuestionsReader questionsReader = new QuestionsReader("/questionsTest2.csv");
                });
        assertEquals("Csv string contains invalid number of options" +
                " or wrong right answer", exception.getMessage());
    }

    @DisplayName("должен выкидывать QuestionsReaderException в случае, если в строке вопроса - пустая строка")
    @Test
    void shouldThrowExceptionIfStringIsEmpty() {

        Exception exception = assertThrows(QuestionsReaderException.class,
                ()->{
                    QuestionsReader questionsReader = new QuestionsReader("/questionsTest3.csv");
                });
        assertEquals("Csv file contains empty strings", exception.getMessage());
    }

    @DisplayName("должен выкидывать QuestionsReaderException в случае, если в строке значение null")
    @Test
    void shouldThrowExceptionIfStringIsNull() {

        Exception exception = assertThrows(QuestionsReaderException.class,
                ()->{
                    QuestionsReader questionsReader = new QuestionsReader("/questionsTest4.csv");
                });
        assertEquals("Csv file contains null values", exception.getMessage());
    }

    @DisplayName("должен выкидывать NumberFormatException в случае, если значение правильного ответа - не число")
    @Test
    void shouldThrowExceptionIfRightAnswerIsNotNumeric() {

        Exception exception = assertThrows(NumberFormatException.class,
                ()->{
                    QuestionsReader questionsReader = new QuestionsReader("/questionsTest5.csv");
                });
    }
}