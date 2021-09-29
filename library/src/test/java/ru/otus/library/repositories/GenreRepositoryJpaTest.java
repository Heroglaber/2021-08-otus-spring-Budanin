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
    TestEntityManager em;

    @Autowired
    GenreRepository genreRepository;

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
        long id = EXISING_GENRE_ID;
        String name = EXISING_GENRE_NAME;
        Genre expectedGenre = new Genre(name);
        expectedGenre.setId(id);

        Genre actualGenre = genreRepository.findById(id).orElseThrow();
        assertThat(expectedGenre).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualGenre);
    }

    @DisplayName("find genre by name")
    @Test
    void shouldFindGenreByName() {
        long id = EXISING_GENRE_ID;
        String name = EXISING_GENRE_NAME;
        Genre expectedGenre = new Genre(name);
        expectedGenre.setId(id);

        Genre actualGenre = genreRepository.findByName(name).orElseThrow();
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
        long id = EXISING_GENRE_ID;
        Genre deletedGenre = em.find(Genre.class, id);
        assertThat(deletedGenre).isNotNull().extracting("id", "name")
                .containsExactly(EXISING_GENRE_ID, EXISING_GENRE_NAME);

        genreRepository.deleteById(id);
        assertThat(em.find(Genre.class, id)).isNull();
    }
}
