package ru.otus.quiz.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.quiz.domain.User;
import ru.otus.quiz.service.Authorization;
import ru.otus.quiz.service.MessageService;
import ru.otus.quiz.service.Quiz;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {
    private final Authorization authorization;

    private final MessageService messageService;

    private final Quiz quiz;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption(defaultValue = "Anonymous") String userName) {
        authorization.login(new User(userName));
        return messageService.getMessage("login_success",
                new String[] {authorization.getAuthorizedUser().getName()}) + "\n";
    }

    @ShellMethod(value = "Logout command", key = {"lo", "logout"})
    @ShellMethodAvailability(value = "isUserLoggedIn")
    public String logout() {
        User user = authorization.getAuthorizedUser();
        authorization.logout();
        return messageService.getMessage("logout_success",
                new String[] {user.getName()}) + "\n";
    }

    @ShellMethod(value = "Start quiz command", key = {"quiz", "start"})
    @ShellMethodAvailability(value = "isUserLoggedIn")
    public void startQuiz() {
        quiz.start();
    }

    private Availability isUserLoggedIn() {
        return authorization.isLoggedIn()? Availability.available():
                Availability.unavailable(messageService.getMessage("access_denied",
                new String[] {}) + "\n");
    }
}
