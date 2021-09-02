package ru.otus.library.services;

import ru.otus.library.models.Author;
import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book findBookById(long id);
    Book findBookByTitle(String title);
    Book addBook(Book book);
    Book addBookAuthor(Book book, Author author);
    Book addBookGenre(Book book, Genre genre);
    Book addBookComment(Book book, Comment comment);
    Book changeBookTitle(Book book, String newTitle);
    Book deleteBookAuthor(Book book, Author author);
    Book deleteBookGenre(Book book, Genre genre);
    Book deleteBookComment(Book book, Comment comment);
    Book deleteBookById(long id);
}
