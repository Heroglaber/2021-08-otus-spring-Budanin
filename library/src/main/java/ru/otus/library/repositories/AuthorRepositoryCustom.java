package ru.otus.library.repositories;

import ru.otus.library.models.domain.Author;
import ru.otus.library.models.domain.Book;

import java.util.List;

public interface AuthorRepositoryCustom {
    List<Book> getBooks(Author author);
    void deleteAuthorRefFromBooks(Author author);
    void deleteBookRefFromAuthor(Author author, Book book);
    void addBookRefToAuthor(Author author, Book book);
}
