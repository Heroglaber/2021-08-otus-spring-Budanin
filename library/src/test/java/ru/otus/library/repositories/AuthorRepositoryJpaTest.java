package ru.otus.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.library.models.domain.Author;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Author repository should ")
@DataJpaTest
public class AuthorRepositoryJpaTest {
    private static final long EXISING_AUTHOR_ID = 3L;
    private static final String EXISTING_AUTHOR_NAME = "Gabriel Garcia Marquez";
    private static final int AUTHORS_TABLE_SIZE = 10;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("correctly save author")
    @Test
    void shouldSaveAuthor() {
        String name = "Test Author";
        Author expectedAuthor = new Author(name);
        authorRepository.save(expectedAuthor);

        assertThat(expectedAuthor.getId()).isGreaterThan(0);

        Author actualAuthor = em.find(Author.class, expectedAuthor.getId());
        assertThat(expectedAuthor).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualAuthor);
    }

    @DisplayName("find author by id")
    @Test
    void shouldFindAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHOR_NAME);
        expectedAuthor.setId(EXISING_AUTHOR_ID);

        Author actualAuthor = authorRepository.findById(EXISING_AUTHOR_ID).orElseThrow();
        assertThat(expectedAuthor).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualAuthor);
    }

    @DisplayName("find author by name")
    @Test
    void shouldFindAuthorByName() {
        Author expectedAuthor = new Author(EXISTING_AUTHOR_NAME);
        expectedAuthor.setId(EXISING_AUTHOR_ID);

        Author actualAuthor = authorRepository.findByName(EXISTING_AUTHOR_NAME).orElseThrow();
        assertThat(expectedAuthor).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualAuthor);
    }

    @DisplayName("find all authors")
    @Test
    void findAll() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors.size()).isEqualTo(AUTHORS_TABLE_SIZE);
    }

    @DisplayName("delete author by id")
    @Test
    void deleteById(){
        long id = EXISING_AUTHOR_ID;
        Author deletedAuthor = em.find(Author.class, id);
        assertThat(deletedAuthor).isNotNull().extracting("id", "name")
                .containsExactly(EXISING_AUTHOR_ID, EXISTING_AUTHOR_NAME);

        authorRepository.deleteById(id);
        assertThat(em.find(Author.class, id)).isNull();
    }
}
