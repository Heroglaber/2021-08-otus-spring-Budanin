package ru.otus.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.library.models.converters.AuthorMapperImpl;
import ru.otus.library.models.converters.BookMapperImpl;
import ru.otus.library.models.converters.CommentMapperImpl;
import ru.otus.library.models.converters.GenreMapperImpl;
import ru.otus.library.models.domain.Author;
import ru.otus.library.services.AuthorServiceImpl;
import ru.otus.library.services.BookServiceImpl;
import ru.otus.library.services.CommentServiceImpl;
import ru.otus.library.services.GenreServiceImpl;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Author repository should ")
@DataMongoTest
@Import({ BookServiceImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, CommentMapperImpl.class, GenreMapperImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class, CommentServiceImpl.class})
public class AuthorRepositoryTest {
    private static final String EXISTING_AUTHOR_NAME = "Gabriel Garcia Marquez";
    private static final int AUTHORS_TABLE_SIZE = 11;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("find all authors")
    @Test
    void findAll() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors.size()).isEqualTo(AUTHORS_TABLE_SIZE);
    }

    @DisplayName("find author by id")
    @Test
    void shouldFindAuthorById() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(EXISTING_AUTHOR_NAME));
        Author expectedAuthor = mongoTemplate.find(query, Author.class).get(0);

        Author actualAuthor = authorRepository.findById(expectedAuthor.getId()).orElseThrow();
        assertThat(expectedAuthor).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualAuthor);
    }

    @DisplayName("find author by name")
    @Test
    void shouldFindAuthorByName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(EXISTING_AUTHOR_NAME));
        Author expectedAuthor = mongoTemplate.find(query, Author.class).get(0);

        Author actualAuthor = authorRepository.findByName(EXISTING_AUTHOR_NAME).orElseThrow();
        assertThat(expectedAuthor).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualAuthor);
    }

    @DisplayName("correctly save author")
    @Test
    void shouldSaveAuthor() {
        String name = "Test Author";
        Author expectedAuthor = new Author(name);
        authorRepository.save(expectedAuthor);

        assertThat(expectedAuthor.getId()).isNotBlank();

        Author actualAuthor = mongoTemplate.findById(expectedAuthor.getId(), Author.class);
        assertThat(expectedAuthor).usingRecursiveComparison().ignoringAllOverriddenEquals()
                .isEqualTo(actualAuthor);
    }

    @DisplayName("delete author by id")
    @Test
    void deleteById(){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(EXISTING_AUTHOR_NAME));
        Author expectedAuthor = mongoTemplate.find(query, Author.class).get(0);
        String id = expectedAuthor.getId();

        Author deletedAuthor = mongoTemplate.findById(id, Author.class);
        assertThat(deletedAuthor).extracting("name")
                .isEqualTo(EXISTING_AUTHOR_NAME);

        authorRepository.deleteById(id);
        assertThat(mongoTemplate.findById(id, Author.class)).isNull();
    }
}
