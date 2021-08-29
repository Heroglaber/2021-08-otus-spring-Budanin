package ru.otus.library.service;

import ru.otus.library.domain.Book;
import ru.otus.library.dto.BookDTO;

import java.util.List;

public interface BookService {
    BookDTO addBook(BookDTO book);

    BookDTO updateBook(BookDTO book);

    BookDTO addBookAuthors(long bookId, String... authorNames);

    void deleteBookByTitle(String title);

    BookDTO findByTitle(String title);

    BookDTO findById(long id);

    List<BookDTO> getAll();

    List<BookDTO> getAllByAuthor(String authorName);

    List<BookDTO> getAllByGenre(String genreName);
}
