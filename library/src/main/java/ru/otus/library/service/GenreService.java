package ru.otus.library.service;

import ru.otus.library.domain.Genre;

public interface GenreService {
    Genre findByName(String genreName);

    Genre add(Genre genre);
}
