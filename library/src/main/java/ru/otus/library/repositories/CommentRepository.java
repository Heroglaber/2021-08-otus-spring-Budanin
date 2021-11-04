package ru.otus.library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.library.models.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Optional<Comment> findById(String id);
    Optional<Comment> findByMessage(String message);
    @Query("{'book.id':?0}")
    List<Comment> findAllByBook_Id( String id);
    void deleteAllByBook_Id(String id);
}
