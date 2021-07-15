package ru.otus.quiz;

import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.List;

public class Question {
    private final String question;
    private final String[] options;
    private final int rightAnswer;

    public Question(String question, String[] options, int rightAnswer) {
        this.question = question;
        this.options = options;
        this.rightAnswer = rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }
}
