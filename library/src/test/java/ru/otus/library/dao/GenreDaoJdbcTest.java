package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.library.dao.GenreDaoJdbc;
import ru.otus.library.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GenreDaoJdbc must ")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    private static final int EXPECTED_GENRES_COUNT = 4;
    private static final int EXISTING_GENRE_ID = 2;
    private static final String EXISTING_GENRE_NAME = "detective";
    private static final int EXISTING_GENRE_ID_2 = 4;
    private static final String EXISTING_GENRE_NAME_2 = "fantasy";


    @Autowired
    private GenreDaoJdbc genreDao;

    @DisplayName("return the expected number of genres")
    @Test
    void shouldReturnExpectedGenreCount() {
        int actualGenresCount = genreDao.count();
        assertThat(actualGenresCount).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("insert genre in database")
    @Test
    void shouldInsertGenre() {
        Genre expectedGenre = new Genre(5, "visual novel");
        genreDao.insert(expectedGenre);
        Genre actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("return expected genre by id")
    @Test
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);
        Genre actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("delete genre by id")
    @Test
    void shouldCorrectDeleteGenreById() {
        assertThat(genreDao.getById(EXISTING_GENRE_ID_2))
                .isInstanceOf(Genre.class);

        genreDao.deleteById(EXISTING_GENRE_ID_2);

        assertThat(genreDao.getById(EXISTING_GENRE_ID_2))
                .isNull();
    }

    @DisplayName("return expected list of genres")
    @Test
    void shouldReturnExpectedGenresList() {
        List<Genre> actualGenreList = genreDao.getAll();
        assertThat(actualGenreList)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(new Genre(1L, "classic"),
                        new Genre(2L, "detective"),
                        new Genre(3L, "horror"),
                        new Genre(4L, "fantasy"));
    }

}