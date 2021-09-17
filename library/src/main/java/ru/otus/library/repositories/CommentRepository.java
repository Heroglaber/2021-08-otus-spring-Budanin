package ru.otus.library.repositories;

import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> findById(long id);

    List<Comment> findByBook(Book book);

    List<Comment> findAll();

    void updateCommentById(long id, String title);

    void deleteById(long id);
}
