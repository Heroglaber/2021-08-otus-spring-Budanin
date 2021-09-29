package ru.otus.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.library.models.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findById(long id);
    List<Book> findByTitle(String title);
}
