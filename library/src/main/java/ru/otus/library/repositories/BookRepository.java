package ru.otus.library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.models.domain.Book;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends MongoRepository<Book, Long> {
    List<Book> findByTitle(String title);
}
