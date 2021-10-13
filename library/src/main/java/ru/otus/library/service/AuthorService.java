package ru.otus.library.service;

import ru.otus.library.domain.Author;

public interface AuthorService {
    Author findByName(String authorName);

    Author findById(long id);

    Author add(Author author);
}
