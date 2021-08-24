package ru.otus.library.dao;

import ru.otus.library.domain.Author;

import java.util.List;

public interface AuthorDao {
    int count();

    Author insert(Author author);

    Author insert(String authorName);

    Author getById(long id);

    Author getByName(String name);

    List<Author> getAll();

    void deleteById(long id);
}
