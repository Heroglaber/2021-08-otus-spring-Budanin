package ru.otus.library.repositories;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.library.models.Author;
import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JpaBookRepository")
@DataJpaTest
@Import(BookRepositoryJpa.class)
public class BookRepositoryJpaTest {
    private static final long FIRST_BOOK_ID = 1L;
    private static final String FIRST_BOOK_NAME = "The Roadside Picnic";
    private static final String FIRST_BOOK_AUTHOR1 = "Arkady Strugatsky";
    private static final String FIRST_BOOK_AUTHOR2 = "Boris Strugatsky";
    private static final long EXPECTED_QUERIES_COUNT = 2;
    private static final int EXPECTED_NUMBER_OF_BOOKS = 10;
    private static final String NEW_AUTHOR_NAME_FIRST = "Stephen King";
    private static final String NEW_AUTHOR_NAME_SECOND = "Peter Straub";
    private static final String NEW_GENRE_NAME_FIRST= "romance";
    private static final String NEW_GENRE_NAME_SECOND= "thriller";
    private static final String NEW_BOOK_NAME = "The Talisman";

    @Autowired
    private BookRepositoryJpa bookRepository;
    @Autowired
    private TestEntityManager em;

    @DisplayName("should save book with all info")
    @Test
    void shouldSaveAllBookInfo() {
        val author1 = new Author(0, NEW_AUTHOR_NAME_FIRST);
        val author2 = new Author(0, NEW_AUTHOR_NAME_SECOND);
        List<Author> authors = List.of(author1, author2);
        val genre1 = new Genre(0, NEW_GENRE_NAME_FIRST);
        val genre2 = new Genre(0, NEW_GENRE_NAME_SECOND);
        List<Genre> genres = List.of(genre1, genre2);
        val book = new Book(0, NEW_BOOK_NAME, authors, genres);
        bookRepository.save(book);
        em.flush();
        em.detach(book);
        assertThat(book.getId()).isGreaterThan(0);

        val actualBook = em.find(Book.class, book.getId());
        assertThat(actualBook).isNotNull().matches(b -> b.getTitle().equals(NEW_BOOK_NAME))
                .matches(b -> b.getAuthors() != null && b.getAuthors().size() == 2 && b.getAuthors().get(0).getId() > 0)
                .matches(b -> b.getGenres() != null && b.getGenres().size() == 2 && b.getGenres().get(1).getName().equals(NEW_GENRE_NAME_SECOND));
    }

    @DisplayName("should load book by id")
    @Test
    void shouldFindExpectedBookById() {
        val optionalActualBook = bookRepository.findById(FIRST_BOOK_ID);
        val expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(optionalActualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
        assertThat(optionalActualBook.get())
                .matches(book -> book.getAuthors() != null && book.getAuthors().size() > 0)
                .matches(book -> book.getGenres() != null && book.getGenres().size() > 0);
    }

    @DisplayName("should load all books with all joined entities")
    @Test
    void shouldReturnCorrectBooksListWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);


        System.out.println("\n\n\n\n----------------------------------------------------------------------------------------------------------");
        val books = bookRepository.findAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(book -> !book.getTitle().equals(""))
                .allMatch(book -> book.getAuthors() != null && book.getAuthors().size() > 0)
                .allMatch(book -> book.getGenres() != null && book.getGenres().size() > 0);
        System.out.println("----------------------------------------------------------------------------------------------------------\n\n\n\n");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @DisplayName("should return expected book with given title")
    @Test
    void shouldFindExpectedBookByTitle() {
        List<Book> booksWithGivenTitle = bookRepository.findByTitle(FIRST_BOOK_NAME);
        val expectedBook = em.find(Book.class, 1L);
        assertThat(booksWithGivenTitle).containsExactly(expectedBook);
        assertThat(booksWithGivenTitle).isNotNull()
                .allMatch(book -> book.getAuthors() != null && book.getAuthors().size() > 0)
                .allMatch(book -> book.getGenres() != null && book.getGenres().size() > 0);
        assertThat(booksWithGivenTitle.get(0).getAuthors()
                .stream()
                .map(Author::getName)
                .collect(Collectors.toList()))
                .containsExactlyInAnyOrder(FIRST_BOOK_AUTHOR1, FIRST_BOOK_AUTHOR2);
    }

    @DisplayName("should correctly update title by given id")
    @Test
    void shouldUpdateTitleById() {
        val bookBeforeUpdate = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(bookBeforeUpdate.getTitle()).isEqualTo(FIRST_BOOK_NAME);

        String newBookTitle = "The Updated Title";

        em.detach(bookBeforeUpdate);

        bookRepository.updateTitleById(FIRST_BOOK_ID, newBookTitle);

        val bookAfterUpdate = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(bookAfterUpdate.getTitle()).isEqualTo(newBookTitle);
    }

    @DisplayName("should delete book by id")
    @Test
    void shouldDeleteBookById() {
        val bookBefore = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(bookBefore).isNotNull().isInstanceOf(Book.class);

        em.detach(bookBefore);

        bookRepository.deleteById(FIRST_BOOK_ID);

        val bookAfter = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(bookAfter).isNull();
    }
}
