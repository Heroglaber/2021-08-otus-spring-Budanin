package ru.otus.quiz.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.exceptions.QuestionsReaderException;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class QuestionDaoCSV implements QuestionDao{
    //To get valid Question object from csv, csv string must have at least 3 values: question string,
    //at least 1 option and answer
    private static final int MIN_VALUES_NUM = 3;

    private final Resource csvFile;

    private List<Question> questions;

    public QuestionDaoCSV(Resource csvFile) {
        this.csvFile = csvFile;
    }

    @Override
    public List<Question> getAll() {
        this.questions = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                csvFile.getInputStream()))) {
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
                List<String> options = Arrays.asList(
                        Arrays.copyOfRange(lineInArray, 1, lineInArray.length - 1));
                if((rightAnswer < 1) || (rightAnswer > options.size())) {
                    throw new QuestionsReaderException("Csv string contains invalid number of options" +
                            " or wrong right answer");
                }
                questions.add(new Question(question, options, rightAnswer));
            }
        } catch (IOException e) {
            throw new QuestionsReaderException("Can't read csv file");
        } catch (CsvValidationException e) {
            throw new QuestionsReaderException("Invalid csv file");
        }

        return questions;
    }
}
