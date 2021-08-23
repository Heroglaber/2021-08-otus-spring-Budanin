package ru.otus.quiz.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.otus.quiz.domain.User;

@Service
@RequiredArgsConstructor
public class AuthorizationEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishUserLogIn(User user) {
        publisher.publishEvent(new UserLoggedInEvent(this, user));
    }

    public void publishUserLogOut() {
        publisher.publishEvent(new UserLoggedOutEvent(this));
    }
}
