package ru.otus.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("After initialization database must ")
@JdbcTest
public class DataInitializationTest {
    private static final int EXPECTED_AUTHORS_COUNT = 10;
    private static final int EXPECTED_GENRES_COUNT = 6;
    private static final int EXPECTED_BOOK_COUNT = 10;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("return expected amount of authors")
    @Test
    void shouldReturnExpectedAuthorsCount() {
        int actualAuthorsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM AUTHORS", Integer.class);
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("return expected amount of genres")
    @Test
    void shouldReturnExpectedGenresCount() {
        int actualGenresCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM GENRES", Integer.class);
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("return expected amount of books")
    @Test
    void shouldReturnExpectedBooksCount() {
        int actualBooksCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BOOKS", Integer.class);
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOK_COUNT);
    }

    @DisplayName("contain specific authors")
    @Test
    void shouldContainExactlyAuthors() {
        List resultList = jdbcTemplate.queryForList("SELECT ID, NAME FROM AUTHORS");
        assertThat(resultList).contains(
                Map.of("ID", Long.valueOf(1), "NAME", "Arkady Strugatsky"),
                Map.of("ID", Long.valueOf(2), "NAME", "Boris Strugatsky")
        );
    }

    @DisplayName("join tables in the correct way")
    @Test
    void shouldJoinTablesInCorrectWay() {
        List resultList = jdbcTemplate.queryForList(
                "select books.id as book_id,\n" +
                        "books.title,\n" +
                        "genres.name as genre,\n" +
                        "authors.name as author,\n" +
                        "from books\n" +
                        "left join book_authors ba on ba.book_id = books.id\n" +
                        "left join book_genres bg on bg.book_id = books.id\n" +
                        "left join authors on authors.id = ba.author_id\n" +
                        "left join genres on genres.id = bg.genre_id");
        assertThat(resultList).contains(
                Map.of("BOOK_ID", Long.valueOf(1), "TITLE", "The Roadside Picnic", "GENRE", "fantasy", "AUTHOR", "Arkady Strugatsky"),
                Map.of("BOOK_ID", Long.valueOf(1), "TITLE", "The Roadside Picnic", "GENRE", "fantasy", "AUTHOR", "Boris Strugatsky"),
                Map.of("BOOK_ID", Long.valueOf(2), "TITLE", "The Final Circle of Paradise", "GENRE", "fantasy", "AUTHOR", "Arkady Strugatsky"),
                Map.of("BOOK_ID", Long.valueOf(2), "TITLE", "The Final Circle of Paradise", "GENRE", "fantasy", "AUTHOR", "Boris Strugatsky")
        );
    }
}
