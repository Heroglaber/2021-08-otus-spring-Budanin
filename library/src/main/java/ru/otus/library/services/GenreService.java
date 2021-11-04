package ru.otus.library.services;

import ru.otus.library.models.dto.GenreDTO;

import java.util.List;

public interface GenreService {
    List<GenreDTO> getAll();
    GenreDTO get(String name);
    GenreDTO add(String name);
    GenreDTO getOrAdd(String name);
    GenreDTO update(GenreDTO genreDTO);
    GenreDTO delete(long id);
}
