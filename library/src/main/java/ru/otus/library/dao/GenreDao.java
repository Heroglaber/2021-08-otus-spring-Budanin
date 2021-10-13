package ru.otus.library.dao;

import ru.otus.library.domain.Genre;

import java.util.List;

public interface GenreDao {
    int count();

    Genre insert(Genre genre);

    Genre update(Genre genre);

    Genre getById(long id);

    Genre getByName(String name);

    List<Genre> getAll();

    void deleteById(long id);
}
