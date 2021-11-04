package ru.otus.library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.models.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, Long> {
    Optional<Genre> findById(long id);
    Optional<Genre> findByName(String name);
}
