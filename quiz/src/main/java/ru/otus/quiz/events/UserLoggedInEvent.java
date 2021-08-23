package ru.otus.quiz.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.otus.quiz.domain.User;

public class UserLoggedInEvent extends ApplicationEvent {
    @Getter
    private final User user;

    public UserLoggedInEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
