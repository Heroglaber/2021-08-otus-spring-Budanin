package ru.otus.library.repositories;

import ru.otus.library.models.domain.Author;

public interface AuthorRepositoryCustom {
    void deleteAuthorFromBooks(Author author);
}
