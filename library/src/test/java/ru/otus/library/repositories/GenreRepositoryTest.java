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
import ru.otus.library.models.domain.Genre;
import ru.otus.library.services.AuthorServiceImpl;
import ru.otus.library.services.BookServiceImpl;
import ru.otus.library.services.CommentServiceImpl;
import ru.otus.library.services.GenreServiceImpl;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Genre repository should ")
@DataMongoTest
@Import({ MongoConfig.class, BookServiceImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, CommentMapperImpl.class, GenreMapperImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class, CommentServiceImpl.class})
public class GenreRepositoryTest {
    private static final String EXISTING_GENRE_NAME = "horror";
    private static final int GENRES_TABLE_SIZE = 7;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GenreRepository genreRepository;


    @DisplayName("find all genres")
    @Test
    void findAll() {
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres.size()).isEqualTo(GENRES_TABLE_SIZE);
    }

    @DisplayName("correctly save genre")
    @Test
    void shouldSaveGenre() {
        String name = "Test Genre";
        Genre expectedGenre = new Genre(name);
        genreRepository.save(expectedGenre);

        assertThat(expectedGenre.getId()).isNotBlank();

        Genre actualGenre = mongoTemplate.findById(expectedGenre.getId(), Genre.class);
                assertThat(expectedGenre).usingRecursiveComparison().ignoringAllOverriddenEquals()
                        .isEqualTo(actualGenre);
    }

    @DisplayName("find genre by id")
    @Test
    void shouldFindGenreById() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(EXISTING_GENRE_NAME));
        Genre expectedGenre = mongoTemplate.find(query, Genre.class).get(0);

        Genre actualGenre = genreRepository.findById(expectedGenre.getId()).orElseThrow();
        assertThat(expectedGenre).usingRecursiveComparison().ignoringAllOverriddenEquals()
                .isEqualTo(actualGenre);
    }

    @DisplayName("find genre by name")
    @Test
    void shouldFindGenreByName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(EXISTING_GENRE_NAME));
        Genre expectedGenre = mongoTemplate.find(query, Genre.class).get(0);

        Genre actualGenre = genreRepository.findByName(expectedGenre.getName()).orElseThrow();
        assertThat(expectedGenre).usingRecursiveComparison().ignoringAllOverriddenEquals()
                .isEqualTo(actualGenre);
    }

    @DisplayName("delete genre by id")
    @Test
    void deleteById(){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(EXISTING_GENRE_NAME));
        Genre expectedGenre = mongoTemplate.find(query, Genre.class).get(0);
        String id = expectedGenre.getId();

        Genre deletedGenre = mongoTemplate.findById(id, Genre.class);
        assertThat(deletedGenre).extracting("name")
                .isEqualTo(EXISTING_GENRE_NAME);

        genreRepository.deleteById(id);
        assertThat(mongoTemplate.findById(id, Genre.class)).isNull();
    }
}
