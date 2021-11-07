package ru.otus.library.repositories;

import ru.otus.library.models.domain.Book;

public interface BookRepositoryCustom {
    void addBookRefToAuthors(Book book);
    void deleteBookRefFromAuthors(Book book);
}
