package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthorDaoJdbc must ")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    private static final int EXPECTED_AUTHORS_COUNT = 2;
    private static final int EXISTING_AUTHOR_ID = 2;
    private static final String EXISTING_AUTHOR_NAME = "Boris Strugatsky";
    private static final int EXISTING_AUTHOR_ID_2 = 1;
    private static final String EXISTING_AUTHOR_NAME_2 = "Arkady Strugatsky";


    @Autowired
    private AuthorDaoJdbc authorDao;

    @DisplayName("return the expected number of authors")
    @Test
    void shouldReturnExpectedAuthorCount() {
        int actualAuthorsCount = authorDao.count();
        assertThat(actualAuthorsCount).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("insert author in database")
    @Test
    void shouldInsertAuthor() {
        Author insertedAuthor = authorDao.insert(new Author("Igor"));
        Author actualAuthor = authorDao.getById(insertedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(insertedAuthor);
    }

    @DisplayName("return expected author by id")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);
        Author actualAuthor = authorDao.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("delete author by id")
    @Test
    void shouldCorrectDeleteAuthorById() {
        assertThat(authorDao.getById(EXISTING_AUTHOR_ID))
                .isInstanceOf(Author.class);

        authorDao.deleteById(EXISTING_AUTHOR_ID);

        assertThat(authorDao.getById(EXISTING_AUTHOR_ID)).isNull();
    }

    @DisplayName("return expected list of authors")
    @Test
    void shouldReturnExpectedAuthorsList() {
        Author expectedAuthor1 = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);
        Author expectedAuthor2 = new Author(EXISTING_AUTHOR_ID_2, EXISTING_AUTHOR_NAME_2);
        List<Author> actualAuthorList = authorDao.getAll();
        assertThat(actualAuthorList)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedAuthor1, expectedAuthor2);
    }

}
