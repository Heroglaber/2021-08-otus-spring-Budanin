package ru.otus.quiz;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ru.otus.quiz.exceptions.QuestionsReaderException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class QuestionsReader {
    //To get valid Question object from csv, csv string must have at least 3 values: question string,
    //at least 1 option and answer
    private static final int MIN_VALUES_NUM = 3;

    //csv filepath
    private final String filePath;

    private List<Question> questions = null;

    public QuestionsReader(String filePath) {
        this.filePath = filePath;
    }

    public List<Question> getQuestions() throws IOException, CsvException {
        if(this.questions == null) {
            readAllQuestions();
        }
        return questions;
    }

    public void readAllQuestions() throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                this.getClass().getResourceAsStream(filePath)))) {
            questions = new ArrayList<>();
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                if(lineInArray.length < MIN_VALUES_NUM) {
                    throw new QuestionsReaderException("Csv string must contain at least 3 values: question string, " +
                            "at least 1 option and answer");
                }
                if(Stream.of(lineInArray).anyMatch(x -> x == null || "".equals(x.trim()))) {
                    throw new QuestionsReaderException("Csv file contains empty strings");
                }
                if(Stream.of(lineInArray).anyMatch(x -> "Null".equals(x.trim()) || "null".equals(x.trim()))) {
                    throw new QuestionsReaderException("Csv file contains null values");
                }
                //first value of csv string must be question
                String question = lineInArray[0];
                //last value of csv string must be right answer
                int rightAnswer = Integer.parseInt(lineInArray[lineInArray.length - 1]);
                //the rest of the values in csv are answer options
                String[] options = Arrays.copyOfRange(lineInArray, 1, lineInArray.length - 1);
                if((rightAnswer < 1) || (rightAnswer > options.length)) {
                    throw new QuestionsReaderException("Csv string contains invalid number of options" +
                            " or wrong right answer");
                }
                questions.add(new Question(question, options, rightAnswer));
            }
        }
    }
}
