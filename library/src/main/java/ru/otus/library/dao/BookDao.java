package ru.otus.library.dao;

import ru.otus.library.domain.Book;

import java.util.List;

public interface BookDao {
    int count();

    Book insert(Book book);

    Book update(Book book);

    Book getById(long id);

    Book getByTitle(String title);

    List<Book> getAll();

    List<Book> getAllByGenre(long genreId);

    void deleteById(long id);
}
