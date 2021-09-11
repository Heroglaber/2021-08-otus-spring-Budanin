package ru.otus.library.services;

import ru.otus.library.dto.BookDto;
import ru.otus.library.models.Author;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();
    BookDto findBookById(long id);
    BookDto findBookByTitle(String title);
    BookDto addBook(BookDto bookDto);
    BookDto addBookAuthor(BookDto bookDto, Author author);
    BookDto addBookGenre(BookDto bookDto, Genre genre);
    BookDto addBookComment(BookDto bookDto, Comment comment);
    BookDto changeBookTitle(BookDto bookDto, String newTitle);
    BookDto deleteBookAuthor(BookDto bookDto, Author author);
    BookDto deleteBookGenre(BookDto bookDto, Genre genre);
    BookDto deleteBookComment(BookDto bookDto, Comment comment);
    BookDto deleteBookById(long id);
}
