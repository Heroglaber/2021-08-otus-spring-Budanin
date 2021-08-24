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
    private static final int EXPECTED_AUTHORS_COUNT = 2;
    private static final int EXPECTED_GENRES_COUNT = 4;
    private static final int EXPECTED_BOOK_COUNT = 2;

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
        assertThat(resultList).containsExactlyInAnyOrder(
                Map.of("ID", Long.valueOf(1), "NAME", "Arkady Strugatsky"),
                Map.of("ID", Long.valueOf(2), "NAME", "Boris Strugatsky")
        );
    }

    @DisplayName("join tables in the correct way")
    @Test
    void shouldJoinTablesInCorrectWay() {
        List resultList = jdbcTemplate.queryForList("SELECT BOOKS.ID as BOOKID,\n" +
                "BOOKS.TITLE,\n" +
                "GENRES.NAME as GENRE,\n" +
                "AUTHORS.NAME as AUTHOR\n" +
                "FROM BOOKS\n" +
                "LEFT JOIN GENRES ON BOOKS.GENREID = GENRES.ID\n" +
                "LEFT JOIN BOOK_AUTHOR BA ON BA.BOOKID = BOOKS.ID\n" +
                "LEFT JOIN AUTHORS ON AUTHORS.ID = BA.AUTHORID");
        assertThat(resultList).containsExactlyInAnyOrder(
                Map.of("BOOKID", Long.valueOf(1), "TITLE", "The Roadside Picnic", "GENRE", "fantasy", "AUTHOR", "Arkady Strugatsky"),
                Map.of("BOOKID", Long.valueOf(1), "TITLE", "The Roadside Picnic", "GENRE", "fantasy", "AUTHOR", "Boris Strugatsky"),
                Map.of("BOOKID", Long.valueOf(2), "TITLE", "The Final Circle of Paradise", "GENRE", "fantasy", "AUTHOR", "Arkady Strugatsky"),
                Map.of("BOOKID", Long.valueOf(2), "TITLE", "The Final Circle of Paradise", "GENRE", "fantasy", "AUTHOR", "Boris Strugatsky")
        );
    }
}
