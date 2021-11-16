package ru.otus.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.library.models.domain.Author;
import ru.otus.library.models.domain.Book;
import ru.otus.library.models.domain.Comment;
import ru.otus.library.models.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("After initialization database must ")
@SpringBootTest
public class DataInitializationTest {
    private static final int EXPECTED_AUTHORS_COUNT = 10;
    private static final int EXPECTED_GENRES_COUNT = 6;
    private static final int EXPECTED_BOOK_COUNT = 10;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("return expected amount of authors")
    @Test
    void shouldReturnExpectedAuthorsCount() {
        Query query = new Query();
        int actualAuthorsCount = (int)mongoTemplate.count(query, Author.class);
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("return expected amount of genres")
    @Test
    void shouldReturnExpectedGenresCount() {
        Query query = new Query();
        int actualGenresCount = (int)mongoTemplate.count(query, Genre.class);
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("return expected amount of books")
    @Test
    void shouldReturnExpectedBooksCount() {
        Query query = new Query();
        int actualBooksCount = (int)mongoTemplate.count(query, Book.class);
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOK_COUNT);
    }

    @DisplayName("contain specific authors")
    @Test
    void shouldContainExactlyAuthors() {
        Query query = new Query();
        List resultList = mongoTemplate.findAll(Author.class);
        assertThat(resultList).extracting("name")
                .contains("Arkady Strugatsky"
                        , "Boris Strugatsky"
                        , "Stephen King");
    }

    @DisplayName("contain specific books")
    @Test
    void shouldContainExactlyBooks() {
        Query query = new Query();
        List resultList = mongoTemplate.findAll(Book.class);
        assertThat(resultList).extracting("title")
                .contains("The Roadside Picnic"
                        , "The Final Circle of Paradise"
                        , "Carrie");
    }

    @DisplayName("contain specific genres")
    @Test
    void shouldContainExactlyGenres() {
        Query query = new Query();
        List resultList = mongoTemplate.findAll(Genre.class);
        assertThat(resultList).extracting("name")
                .contains("classic"
                        , "novel");
    }

    @DisplayName("contain specific comments")
    @Test
    void shouldContainExactlyComments() {
        Query query = new Query();
        List resultList = mongoTemplate.findAll(Comment.class);
        assertThat(resultList).extracting("message")
                .contains("Greatest book ever!"
                        , "JUST AMAZING");
    }
}
