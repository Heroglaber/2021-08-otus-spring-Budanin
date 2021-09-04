package ru.otus.library.dao;

import ru.otus.library.domain.Author;

import java.util.List;

public interface AuthorDao {
    int count();

    Author insert(Author author);

    Author update(Author author);

    Author getById(long id);

    Author getByName(String name);

    List<Author> getAll();

    List<Author> findAllUsed();

    void deleteById(long id);
}
