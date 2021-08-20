package ru.otus.quiz.service;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Scope("singleton")
public class MessageServiceImpl implements MessageService {
    private final MessageSource messageSource;

    private final Locale locale;

    public MessageServiceImpl(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    public String getMessage(String key, Object ...args) throws NoSuchMessageException {
        return messageSource.getMessage(key, args, this.locale);
    }
}
