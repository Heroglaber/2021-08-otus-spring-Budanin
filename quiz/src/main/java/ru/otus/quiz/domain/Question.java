package ru.otus.quiz.domain;

public class Question {
    private final String question;
    private final String[] options;
    private final int correctAnswer;

    public Question(String question, String[] options, int rightAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
