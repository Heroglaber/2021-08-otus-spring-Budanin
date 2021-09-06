package ru.otus.library.service;

import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;

import java.util.List;

public interface BookService {
    Book findById(long id);

    Book findByTitle(String title);

    List<Book> findAll();

    Book add(Book book);

    Book update(Book book);

    Book addAuthor(Book book, Author author);

    Book deleteAuthor(Book book, Author author);

    void delete(Book book);
}
