package ru.otus.library.dao;

import ru.otus.library.domain.Author;

import java.util.List;

public interface AuthorDao {
    int count();

    Author insert(Author author);

    int[] batchInsert(List<Author> authors);

    Author update(Author author);

    int[] batchUpdate(List<Author> authors);

    Author getById(long id);

    Author getByName(String name);

    List<Author> getAll();

    List<Author> findAllUsed();

    void deleteById(long id);
}
