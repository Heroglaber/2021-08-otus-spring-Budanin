package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.library.dao.BookDaoJdbc;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookDaoJdbc must ")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class, BookAuthorRelationDaoJdbc.class})
class BookDaoJdbcTest {

    private static final int EXPECTED_BOOKS_COUNT = 2;
    private static final String EXISTING_BOOK_TITLE = "The Roadside Picnic";
    private static final int EXISTING_BOOK_ID_2 = 2;
    private static final String EXISTING_BOOK_TITLE_2 = "The Final Circle of Paradise";
    private static final String FANTASY_GENRE = "fantasy";
    private static final String BOOK_TITLE = "The Doomed City";
    private static final String GENRE_NAME = "New Genre";
    private static final String FIRST_AUTHOR = "Arkady Strugatsky";
    private static final String SECOND_AUTHOR = "Boris Strugatsky";
    private static final String THIRD_AUTHOR = "JOHN DOE";
    private static final String NEW_TITLE = "Crime and Punishment";

    @Autowired
    private BookDao bookDao;

    @DisplayName("return the expected number of books")
    @Test
    void shouldReturnExpectedBookCount() {
        int actualBooksCount = bookDao.count();
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("insert book in database")
    @Test
    void shouldInsertBook() {
        Genre genre = new Genre(GENRE_NAME);
        Author author1 = new Author(FIRST_AUTHOR);
        Author author2 = new Author(SECOND_AUTHOR);
        Book expectedBook = new Book(3, BOOK_TITLE, genre, List.of(author1, author2));

        assertThat(bookDao.findById(expectedBook.getId())).isNull();
        bookDao.insert(expectedBook);

        Book actualBook = bookDao.findById(expectedBook.getId());
        assertThat(actualBook).isNotNull()
            .matches(book -> { return book.getTitle().equals(BOOK_TITLE); })
            .matches(book -> {return book.getGenre().getName().equals(GENRE_NAME);});
        assertThat(actualBook.getAuthors()).extracting(Author::getName)
                .containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
    }

    @DisplayName("update book in database")
    @Test
    void shouldUpdateBook() {
        Book existingBook = bookDao.findById(EXISTING_BOOK_ID_2);
        assertThat(existingBook.getTitle()).isEqualTo(EXISTING_BOOK_TITLE_2);
        assertThat(existingBook.getAuthors()).extracting(Author::getName).containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
        assertThat(existingBook.getGenre()).extracting(Genre::getName).isEqualTo(FANTASY_GENRE);

        Book newBook = new Book();
        newBook.setId(EXISTING_BOOK_ID_2);
        newBook.setTitle(NEW_TITLE);
        newBook.setGenre(new Genre(GENRE_NAME));
        newBook.setAuthors(List.of(new Author(THIRD_AUTHOR)));
        bookDao.update(newBook);

        Book actualBook = bookDao.findById(EXISTING_BOOK_ID_2);
        assertThat(actualBook.getTitle()).isEqualTo(NEW_TITLE);
        assertThat(actualBook.getAuthors()).extracting(Author::getName).containsExactlyInAnyOrder(THIRD_AUTHOR);
        assertThat(actualBook.getGenre()).extracting(Genre::getName).isEqualTo(GENRE_NAME);
    }

    @DisplayName("return expected book by id")
    @Test
    void shouldReturnExpectedBookById() {
        Book actualBook = bookDao.findById(EXISTING_BOOK_ID_2);
        assertThat(actualBook.getTitle()).isEqualTo(EXISTING_BOOK_TITLE_2);
        assertThat(actualBook.getGenre().getName()).isEqualTo(FANTASY_GENRE);
        assertThat(actualBook.getAuthors()).extracting(Author::getName).containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
    }

    @DisplayName("delete book by id")
    @Test
    void shouldCorrectDeleteBookById() {
        assertThat(bookDao.findById(EXISTING_BOOK_ID_2))
                .isInstanceOf(Book.class);

        bookDao.delete(bookDao.findById(EXISTING_BOOK_ID_2));

        assertThat(bookDao.findById(EXISTING_BOOK_ID_2))
                .isNull();
    }

    @DisplayName("return expected list of books")
    @Test
    void shouldReturnExpectedBooksList() {
        List<Book> actualBookList = bookDao.findAllWithAllInfo();
        assertThat(actualBookList).hasSize(EXPECTED_BOOKS_COUNT)
                .extracting(Book::getTitle).containsExactlyInAnyOrder(EXISTING_BOOK_TITLE, EXISTING_BOOK_TITLE_2);
    }
}
