package ru.otus.library.dao;

import ru.otus.library.domain.BookAuthor;

import java.util.List;

public interface BookAuthorDao {
    void insert(BookAuthor bookAuthor);

    List<BookAuthor> getAllByBookId(long bookId);

    List<BookAuthor> getAllByAuthorId(long authorId);

    void deleteAllByBookId(long bookId);
}
