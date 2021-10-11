package ru.otus.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.library.models.domain.Genre;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Genre repository should ")
@DataJpaTest
public class GenreRepositoryJpaTest {
    private static final long EXISING_GENRE_ID = 3L;
    private static final String EXISING_GENRE_NAME = "horror";
    private static final int GENRES_TABLE_SIZE = 6;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("correctly save genre")
    @Test
    void shouldSaveGenre() {
        String name = "Test Genre";
        Genre expectedGenre = new Genre(name);
        genreRepository.save(expectedGenre);

        assertThat(expectedGenre.getId()).isGreaterThan(0);

        Genre actualGenre = em.find(Genre.class, expectedGenre.getId());
        assertThat(expectedGenre).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualGenre);
    }

    @DisplayName("find genre by id")
    @Test
    void shouldFindGenreById() {
        Genre expectedGenre = new Genre(EXISING_GENRE_NAME);
        expectedGenre.setId(EXISING_GENRE_ID);

        Genre actualGenre = genreRepository.findById(EXISING_GENRE_ID).orElseThrow();
        assertThat(expectedGenre).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualGenre);
    }

    @DisplayName("find genre by name")
    @Test
    void shouldFindGenreByName() {
        Genre expectedGenre = new Genre(EXISING_GENRE_NAME);
        expectedGenre.setId(EXISING_GENRE_ID);

        Genre actualGenre = genreRepository.findByName(EXISING_GENRE_NAME).orElseThrow();
        assertThat(expectedGenre).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualGenre);
    }

    @DisplayName("find all genres")
    @Test
    void findAll() {
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres.size()).isEqualTo(GENRES_TABLE_SIZE);
    }

    @DisplayName("delete genre by id")
    @Test
    void deleteById(){
        Genre deletedGenre = em.find(Genre.class, EXISING_GENRE_ID);
        assertThat(deletedGenre).isNotNull().extracting("id", "name")
                .containsExactly(EXISING_GENRE_ID, EXISING_GENRE_NAME);

        genreRepository.deleteById(EXISING_GENRE_ID);
        assertThat(em.find(Genre.class, EXISING_GENRE_ID)).isNull();
    }
}
