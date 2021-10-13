package ru.otus.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library.models.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(long id);
    Optional<Comment> findByMessage(String message);
    List<Comment> findAllByBook_Id(Long id);
    void deleteAllByBook_Id(Long id);
}
