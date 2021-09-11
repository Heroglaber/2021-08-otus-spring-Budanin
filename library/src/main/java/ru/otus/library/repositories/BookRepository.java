package ru.otus.library.repositories;

import ru.otus.library.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book student);

    Optional<Book> findById(long id);

    List<Book> findAll();

    List<Book> findByTitle(String title);

    void updateTitleById(long id, String title);

    Book deleteById(long id);
}
