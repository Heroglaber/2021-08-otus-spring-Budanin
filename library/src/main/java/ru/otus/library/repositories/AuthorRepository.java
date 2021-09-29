package ru.otus.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library.models.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findById(long id);
    Optional<Author> findByName(String name);
}
