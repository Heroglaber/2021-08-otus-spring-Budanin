package ru.otus.library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.library.models.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, Long> {
    Optional<Comment> findById(long id);
    Optional<Comment> findByMessage(String message);
    @Query("{'book.$id':?0}")
    List<Comment> findAllByBook_Id( Long id);
    void deleteAllByBook_Id(Long id);
}
