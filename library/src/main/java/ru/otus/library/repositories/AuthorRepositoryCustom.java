package ru.otus.library.repositories;

import ru.otus.library.models.domain.Author;
import ru.otus.library.models.domain.Book;

import java.util.List;

public interface AuthorRepositoryCustom {
    List<Book> findBooks(Author author);
    void deleteAuthorFromBooks(Author author);
    void deleteBookFromAuthor(Author author, Book book);
    void addBookToAuthor(Author author, Book book);
}
