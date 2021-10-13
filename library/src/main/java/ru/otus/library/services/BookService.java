package ru.otus.library.services;

import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.GenreDTO;

import java.util.List;

public interface BookService {
    List<BookDTO> getAll();
    BookDTO getById(long id);
    List<BookDTO> getByTitle(String title);
    BookDTO add(BookDTO bookDTO);
    BookDTO addAuthor(BookDTO bookDTO, AuthorDTO author);
    BookDTO addGenre(BookDTO bookDTO, GenreDTO genre);
    BookDTO update(BookDTO bookDTO);
    BookDTO changeTitle(BookDTO bookDTO, String newTitle);
    BookDTO deleteAuthor(BookDTO bookDTO, AuthorDTO author);
    BookDTO deleteGenre(BookDTO bookDTO, GenreDTO genre);
    BookDTO deleteById(long id);
}
