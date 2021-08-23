package ru.otus.quiz.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.domain.User;
import ru.otus.quiz.events.UserLoggedInEvent;
import ru.otus.quiz.events.UserLoggedOutEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("singleton")
public class Quiz {
    private final QuestionService questionService;

    private final MessageService messageService;

    private User authorizedUser;

    private int score;

    private List<Boolean> answers;

    private List<Question> questions;

    private final BufferedReader userInput;

    private final PrintStream userOutput;

    public Quiz(QuestionService questionService, MessageServiceImpl messageService) {
        this.questionService = questionService;
        this.messageService = messageService;
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        this.userOutput = System.out;
    }

    @EventListener
    public void onUserLogIn(UserLoggedInEvent event) {
        this.authorizedUser = event.getUser();
    }

    @EventListener
    public void onUserLogOut(UserLoggedOutEvent event) {
        this.authorizedUser = null;
    }

    public void start(){
        String userName = (authorizedUser != null)?authorizedUser.getName(): "anonymous";
        userOutput.printf(messageService.getMessage("greeting", new String[] {userName}) + "\n");

        this.questions = questionService.getAllQuestions();
        this.answers = new ArrayList<>();
        this.score = 0;
        for(Question question: questions) {
            askQuestion(question);
            readAnswer(question);
        }
        calculateScore();
        userOutput.printf(messageService.getMessage("score", new String[] {String.valueOf(score), String.valueOf(questions.size())}) + "\n");
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
        for( int i = 0; i < question.getOptions().size(); i++) {
            userOutput.printf("%d) %s\n", i + 1, question.getOptions().get(i));
        }
    }

    private void readAnswer(Question question) {
        int answer = 0;
        try{
            answer = Integer.parseInt(userInput.readLine());
        }
        catch (NumberFormatException | IOException e) {
            userOutput.printf(messageService.getMessage("input_error",
                    new String[] {}) + "\n");
            this.readAnswer(question);
            return;
        }
        if((answer < 1) || (answer > question.getOptions().size())) {
            userOutput.printf(messageService.getMessage("input_range_error",
                    new String[] {String.valueOf(question.getOptions().size())}) + "\n");
            this.readAnswer(question);
            return;
        }
        if(answer == question.getCorrectAnswer()) {
            answers.add(true);
        }
        else {
            answers.add(false);
        }
    }
}
