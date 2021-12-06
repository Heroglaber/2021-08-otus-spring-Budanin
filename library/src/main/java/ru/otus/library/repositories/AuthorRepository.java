package ru.otus.library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.models.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends MongoRepository<Author, String>, AuthorRepositoryCustom {
    Optional<Author> findById(String id);
    Optional<Author> findByName(String name);
}
