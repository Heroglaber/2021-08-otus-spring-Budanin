package ru.otus.library.dao;

import ru.otus.library.ex.BookAuthorRelation;

import java.util.List;

public interface BookAuthorRelationDao {
    List<BookAuthorRelation> getAll();

    List<BookAuthorRelation> getByBookId(long bookId);

    List<BookAuthorRelation> getByAuthorId(long authorId);

    BookAuthorRelation get(long bookId, long authorId);

    int[] batchInsert(List<BookAuthorRelation> relations);

    BookAuthorRelation insert(BookAuthorRelation bookAuthorRelation);

    void delete(BookAuthorRelation bookAuthorRelation);

    void deleteAllByBookId(long bookId);
}
