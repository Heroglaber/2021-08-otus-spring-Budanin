package ru.otus.quiz.events;

import org.springframework.context.ApplicationEvent;

public class UserLoggedOutEvent extends ApplicationEvent {

    public UserLoggedOutEvent(Object source) {
        super(source);
    }
}
