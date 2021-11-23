package ru.otus.library.services;

import ru.otus.library.models.dto.GenreDTO;

import java.util.List;

public interface GenreService {
    List<GenreDTO> getAll();
    GenreDTO get(GenreDTO genreDTO);
    GenreDTO add(GenreDTO genreDTO);
    GenreDTO getOrAdd(GenreDTO genreDTO);
    GenreDTO update(GenreDTO genreDTO);
    GenreDTO delete(String id);
}
