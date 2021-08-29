package ru.otus.library.service;

import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;

import java.util.List;

public interface BookAuthorService {
    void addBookAuthor(long bookId, long authorId);

    List<Author> getAllAuthorsOfBook(long bookId);

    List<Book> getAllBooksOfAuthor(long authorId);
}
