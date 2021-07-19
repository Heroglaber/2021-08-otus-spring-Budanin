package ru.otus.quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private int score;

    private List<Boolean> answers;

    private List<Question> questions;

    private BufferedReader userInput;

    private PrintStream userOutput;

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Quiz() {
        this.answers = new ArrayList<>();
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        this.userOutput = System.out;
    }

    public void start(){
        this.score = 0;
        for(Question question: questions) {
            askQuestion(question);
            readAnswer(question);
        }
        calculateScore();
        userOutput.printf("You scored %d points out of %d", score, questions.size());
    }

    private int calculateScore() {
        for (Boolean answer : answers) {
            if (answer) {
                score++;
            }
        }
        return score;
    }

    private void askQuestion(Question question) {
        userOutput.printf("%s\n", question.getQuestion());
        for( int i = 0; i < question.getOptions().length; i++) {
            userOutput.printf("%d) %s\n", i + 1, question.getOptions()[i]);
        }
    }

    private void readAnswer(Question question) {
        int answer = 0;
        try{
            answer = Integer.parseInt(userInput.readLine());
        }
        catch (NumberFormatException | IOException e) {
            userOutput.printf("Enter the numeric answer only\n");
            this.readAnswer(question);
            return;
        }
        if((answer < 1) || (answer > question.getOptions().length)) {
            userOutput.printf("The numeric answer must be between [1, %d]\n", question.getOptions().length);
            this.readAnswer(question);
            return;
        }
        if(answer == question.getRightAnswer()) {
            answers.add(true);
        }
        else {
            answers.add(false);
        }
    }
}
