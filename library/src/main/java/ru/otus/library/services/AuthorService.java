package ru.otus.library.services;

import ru.otus.library.models.dto.AuthorDTO;

import java.util.List;

public interface AuthorService {
    List<AuthorDTO> getAll();
    AuthorDTO get(String name);
    AuthorDTO add(String name);
    AuthorDTO getOrAdd(String name);
    AuthorDTO update(AuthorDTO authorDTO);
    AuthorDTO delete(long id);
}
