package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.library.dao.BookDaoJdbc;
import ru.otus.library.domain.Book;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookDaoJdbc must ")
@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {

    private static final int EXPECTED_BOOKS_COUNT = 2;
    private static final int EXISTING_BOOK_ID = 1;
    private static final String EXISTING_BOOK_TITLE = "The Roadside Picnic";
    private static final int EXISTING_BOOK_ID_2 = 2;
    private static final String EXISTING_BOOK_TITLE_2 = "The Final Circle of Paradise";
    private static final int FANTASY_ID = 4;


    @Autowired
    private BookDaoJdbc bookDao;

    @DisplayName("return the expected number of books")
    @Test
    void shouldReturnExpectedBookCount() {
        int actualBooksCount = bookDao.count();
        assertThat(actualBooksCount).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("insert book in database")
    @Test
    void shouldInsertBook() {
//        Book expectedBook = new Book(3, "The Doomed City", FANTASY_ID);
//        bookDao.insert(expectedBook);
//        Book actualBook = bookDao.getById(expectedBook.getId());
//        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("update book in database")
    @Test
    void shouldUpdateBook() {
//        Book existedBook = bookDao.getById(EXISTING_BOOK_ID_2);
//        assertThat(existedBook)
//                .isEqualTo(new Book(2, "The Final Circle of Paradise", 4));
//
//        String newTitle = "Crime and Punishment";
//        long newGenreId = 1;
//        bookDao.update(new Book(existedBook.getId(), newTitle, newGenreId));
//
//        assertThat(bookDao.getById(EXISTING_BOOK_ID_2))
//                .isEqualTo(new Book(2, "Crime and Punishment", 1));
    }

    @DisplayName("return expected book by id")
    @Test
    void shouldReturnExpectedBookById() {
//        Book expectedBook = new Book(EXISTING_BOOK_ID, EXISTING_BOOK_TITLE, FANTASY_ID);
//        Book actualBook = bookDao.getById(expectedBook.getId());
//        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
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
//        Book expectedBook1 = new Book(EXISTING_BOOK_ID, EXISTING_BOOK_TITLE, FANTASY_ID);
//        Book expectedBook2 = new Book(EXISTING_BOOK_ID_2, EXISTING_BOOK_TITLE_2, FANTASY_ID);
//        List<Book> actualBookList = bookDao.getAll();
//        assertThat(actualBookList)
//                .usingFieldByFieldElementComparator()
//                .containsExactlyInAnyOrder(expectedBook1, expectedBook2);
    }
}
