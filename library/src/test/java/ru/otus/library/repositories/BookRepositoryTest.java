package ru.otus.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.library.configs.MongoConfig;
import ru.otus.library.models.converters.AuthorMapperImpl;
import ru.otus.library.models.converters.BookMapperImpl;
import ru.otus.library.models.converters.CommentMapperImpl;
import ru.otus.library.models.converters.GenreMapperImpl;
import ru.otus.library.models.domain.Book;
import ru.otus.library.services.AuthorServiceImpl;
import ru.otus.library.services.BookServiceImpl;
import ru.otus.library.services.CommentServiceImpl;
import ru.otus.library.services.GenreServiceImpl;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Book repository should ")
@DataMongoTest
@Import({ MongoConfig.class, BookServiceImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, CommentMapperImpl.class, GenreMapperImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class, CommentServiceImpl.class})
public class BookRepositoryTest {
    private static final String EXISTING_BOOK_TITLE = "One Hundred Years of Solitude";
    private static final int BOOKS_TABLE_SIZE = 10;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("correctly save book")
    @Test
    void shouldSaveBook() {
        String name = "Test Book";
        Book expectedBook = new Book(name);

        bookRepository.save(expectedBook);

        assertThat(expectedBook.getId()).isNotBlank();

        Book actualBook = mongoTemplate.findById(expectedBook.getId(), Book.class);
                assertThat(expectedBook).usingRecursiveComparison().ignoringAllOverriddenEquals()
                        .isEqualTo(actualBook);
    }

    @DisplayName("find book by id")
    @Test
    void shouldFindBookById() {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(EXISTING_BOOK_TITLE));
        Book expectedBook = mongoTemplate.find(query, Book.class).get(0);

        Book actualBook = bookRepository.findById(expectedBook.getId()).orElseThrow();

        assertThat(expectedBook).usingRecursiveComparison().ignoringAllOverriddenEquals()
                .ignoringFields("title", "authors", "genres").isEqualTo(actualBook);
    }

    @DisplayName("find book by title")
    @Test
    void shouldFindBookByName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(EXISTING_BOOK_TITLE));
        Book expectedBook = mongoTemplate.find(query, Book.class).get(0);

        Book actualBook = bookRepository.findByTitle(EXISTING_BOOK_TITLE).get(0);

        assertThat(expectedBook).usingRecursiveComparison().ignoringAllOverriddenEquals()
                .ignoringFields("title", "authors", "genres").isEqualTo(actualBook);
    }

    @DisplayName("find all books")
    @Test
    void findAll() {
        List<Book> books = bookRepository.findAll();

        assertThat(books.size()).isEqualTo(BOOKS_TABLE_SIZE);
    }

    @DisplayName("delete book by id")
    @Test
    void deleteById(){
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(EXISTING_BOOK_TITLE));
        Book expectedBook = mongoTemplate.find(query, Book.class).get(0);
        String id = expectedBook.getId();

        Book deletedBook = mongoTemplate.findById(id, Book.class);
        assertThat(deletedBook).extracting("title")
                .isEqualTo(EXISTING_BOOK_TITLE);

        bookRepository.deleteById(id);
        assertThat(mongoTemplate.findById(id, Book.class)).isNull();
    }
}
