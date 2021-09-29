package ru.otus.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.library.models.domain.Author;
import ru.otus.library.models.domain.Book;
import ru.otus.library.models.domain.Genre;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Book repository should ")
@DataJpaTest
public class BookRepositoryJpaTest {
    private static final long EXISTING_BOOK_ID = 3L;
    private static final String EXISING_BOOK_TITLE = "One Hundred Years of Solitude";
    private static final int BOOKS_TABLE_SIZE = 10;

    @Autowired
    TestEntityManager em;

    @Autowired
    BookRepository bookRepository;

    @DisplayName("correctly save book")
    @Test
    void shouldSaveBook() {
        String name = "Test Book";
        Book expectedBook = new Book(name);

        bookRepository.save(expectedBook);

        assertThat(expectedBook.getId()).isGreaterThan(0);
        Book actualBook = em.find(Book.class, expectedBook.getId());
        assertThat(expectedBook).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualBook);
    }

    @DisplayName("correctly save with all fields book")
    @Test
    void shouldSaveBookWithAllFields() {
        String title = "Test Book";
        String autorName = "Test Author";
        String genreName = "Test Genre";
        Book expectedBook = new Book(title);
        expectedBook.setAuthors(List.of(new Author(autorName)));
        expectedBook.setGenres(List.of(new Genre(genreName)));

        bookRepository.save(expectedBook);

        assertThat(expectedBook.getId()).isGreaterThan(0);
        Book actualBook = em.find(Book.class, expectedBook.getId());
        assertThat(expectedBook).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualBook);
        assertThat(expectedBook.getAuthors()).isEqualTo(actualBook.getAuthors());
        assertThat(expectedBook.getGenres()).isEqualTo(actualBook.getGenres());
    }

    @DisplayName("find book by id")
    @Test
    void shouldFindBookById() {
        long id = EXISTING_BOOK_ID;
        String name = EXISING_BOOK_TITLE;
        Book expectedBook = new Book(name);
        expectedBook.setId(id);

        Book actualBook = bookRepository.findById(id).orElseThrow();

        assertThat(expectedBook).usingRecursiveComparison().ignoringAllOverriddenEquals()
                .ignoringFields("authors", "genres").isEqualTo(actualBook);
    }

    @DisplayName("find book by title")
    @Test
    void shouldFindBookByName() {
        long id = EXISTING_BOOK_ID;
        String title = EXISING_BOOK_TITLE;
        Book expectedBook = new Book(title);
        expectedBook.setId(id);

        Book actualBook = bookRepository.findByTitle(title).get(0);

        assertThat(expectedBook).usingRecursiveComparison().ignoringAllOverriddenEquals()
                .ignoringFields("authors", "genres").isEqualTo(actualBook);
    }

    @DisplayName("find all books")
    @Test
    void findAll() {
        List<Book> books = bookRepository.findAll();

        assertThat(books.size()).isEqualTo(BOOKS_TABLE_SIZE);
    }

    @DisplayName("delete book by id")
    @Test
    void deleteById(){
        long id = EXISTING_BOOK_ID;
        Book deletedBook = em.find(Book.class, id);
        assertThat(deletedBook).isNotNull().extracting("id", "title")
                .containsExactly(EXISTING_BOOK_ID, EXISING_BOOK_TITLE);

        bookRepository.deleteById(id);

        assertThat(em.find(Book.class, id)).isNull();
    }
}
