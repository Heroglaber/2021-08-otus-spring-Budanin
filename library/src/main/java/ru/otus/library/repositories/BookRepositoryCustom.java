package ru.otus.library.repositories;

import ru.otus.library.models.domain.Book;

public interface BookRepositoryCustom {
    void addBookRefToAuthors(String bookId);
}
