package ru.otus.quiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.quiz.domain.User;
import ru.otus.quiz.events.AuthorizationEventPublisher;
import ru.otus.quiz.exceptions.AuthorizationException;

@Component
@RequiredArgsConstructor
public class Authorization {

    private final AuthorizationEventPublisher eventsPublisher;

    private User authorizedUser;

    private boolean isLoggedIn = false;

    public void login(User user){
        if(!isLoggedIn) {
            authorizedUser = user;
            isLoggedIn = true;
            eventsPublisher.publishUserLogIn(user);
        }
        else throw new AuthorizationException("User is already logged in!");
    }

    public void logout() {
        if(isLoggedIn) {
            authorizedUser = null;
            isLoggedIn = false;
            eventsPublisher.publishUserLogOut();
        }
        else throw new AuthorizationException("User is already logged out!");
    }

    public User getAuthorizedUser() {
        if(authorizedUser != null) {
            return authorizedUser;
        }
        else throw new AuthorizationException("User is not logged in!");
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
