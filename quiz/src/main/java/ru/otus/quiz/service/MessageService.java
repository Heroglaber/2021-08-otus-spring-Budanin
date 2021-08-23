package ru.otus.quiz.service;

import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.Nullable;

public interface MessageService {
    String getMessage(String key, Object ...args) throws NoSuchMessageException;
}
