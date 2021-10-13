package ru.otus.library.dao;

import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;

import java.util.List;

public interface BookDao {
    int count();

    List<Book> findAllWithAllInfo();

    Book insert(Book book);

    Book update(Book book);

    Book addAuthor(Book book, Author author);

    Book deleteAuthor(Book book, Author author);

    void delete(Book book);

    Book findById(long id);

    Book findByTitle(String title);
}
