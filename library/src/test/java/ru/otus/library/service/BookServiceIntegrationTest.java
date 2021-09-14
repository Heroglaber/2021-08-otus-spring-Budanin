package ru.otus.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookService must ")
@SpringBootTest
@Transactional
public class BookServiceIntegrationTest {
    private static final String FANTASY_GENRE = "fantasy";
    private static final String BOOK_TITLE = "The Final Circle of Paradise";
    private static final int EXPECTED_BOOKS_COUNT = 2;
    private static final String EXISTING_BOOK_TITLE = "The Roadside Picnic";
    private static final int EXISTING_BOOK_ID_2 = 2;
    private static final String EXISTING_BOOK_TITLE_2 = "The Final Circle of Paradise";
    private static final String GENRE_NAME = "New Genre";
    private static final String FIRST_AUTHOR = "Arkady Strugatsky";
    private static final String SECOND_AUTHOR = "Boris Strugatsky";
    private static final String THIRD_AUTHOR = "JOHN DOE";
    private static final String NEW_TITLE = "Crime and Punishment";

    @Autowired
    private BookService bookService;

    @DisplayName("find expected book by id")
    @Test
    void shouldFindExpectedBookById() {
        Book actualBook = bookService.findById(EXISTING_BOOK_ID_2);
        assertThat(actualBook.getTitle()).isEqualTo(EXISTING_BOOK_TITLE_2);
        assertThat(actualBook.getGenre().getName()).isEqualTo(FANTASY_GENRE);
        assertThat(actualBook.getAuthors()).extracting(Author::getName).containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
    }

    @DisplayName("find expected book by title")
    @Test
    void shouldFindExpectedBookByTitle() {
        Book actualBook = bookService.findByTitle(EXISTING_BOOK_TITLE);
        assertThat(actualBook.getTitle()).isEqualTo(EXISTING_BOOK_TITLE);
        assertThat(actualBook.getGenre().getName()).isEqualTo(FANTASY_GENRE);
        assertThat(actualBook.getAuthors()).extracting(Author::getName).containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
    }

    @DisplayName("find all books in library")
    @Test
    void shouldReturnAllBooksList() {
        List<Book> actualBookList = bookService.findAll();
        assertThat(actualBookList).hasSize(EXPECTED_BOOKS_COUNT)
                .extracting(Book::getTitle).containsExactlyInAnyOrder(EXISTING_BOOK_TITLE, EXISTING_BOOK_TITLE_2);
    }

    @DisplayName("add book to library")
    @Test
    void shouldAddBookToLibrary() {
        Genre genre = new Genre(GENRE_NAME);
        Author author1 = new Author(FIRST_AUTHOR);
        Author author2 = new Author(SECOND_AUTHOR);
        Book expectedBook = new Book(3, BOOK_TITLE, genre, List.of(author1, author2));

        assertThat(bookService.findById(expectedBook.getId())).isNull();
        bookService.add(expectedBook);

        Book actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook).isNotNull()
                .matches(book -> { return book.getTitle().equals(BOOK_TITLE); })
                .matches(book -> {return book.getGenre().getName().equals(GENRE_NAME);});
        assertThat(actualBook.getAuthors()).extracting(Author::getName)
                .containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
    }

    @DisplayName("update book in library")
    @Test
    void shouldUpdateBook() {
        Book existingBook = bookService.findById(EXISTING_BOOK_ID_2);
        assertThat(existingBook.getTitle()).isEqualTo(EXISTING_BOOK_TITLE_2);
        assertThat(existingBook.getAuthors()).extracting(Author::getName).containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);
        assertThat(existingBook.getGenre()).extracting(Genre::getName).isEqualTo(FANTASY_GENRE);

        Book newBook = new Book();
        newBook.setId(EXISTING_BOOK_ID_2);
        newBook.setTitle(NEW_TITLE);
        newBook.setGenre(new Genre(GENRE_NAME));
        newBook.setAuthors(List.of(new Author(THIRD_AUTHOR)));
        bookService.update(newBook);

        Book actualBook = bookService.findById(EXISTING_BOOK_ID_2);
        assertThat(actualBook.getTitle()).isEqualTo(NEW_TITLE);
        assertThat(actualBook.getAuthors()).extracting(Author::getName).containsExactlyInAnyOrder(THIRD_AUTHOR);
        assertThat(actualBook.getGenre()).extracting(Genre::getName).isEqualTo(GENRE_NAME);
    }

    @DisplayName("delete book author")
    @Test
    void shouldDeleteBookAuthor() {
        Book bookBefore = bookService.findById(EXISTING_BOOK_ID_2);
        assertThat(bookBefore.getAuthors()).extracting(Author::getName)
                .containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);

        bookService.deleteAuthor(bookBefore, bookBefore.getAuthors().get(1));

        Book bookAfter = bookService.findById(EXISTING_BOOK_ID_2);
        assertThat(bookBefore.getAuthors()).extracting(Author::getName)
                .containsExactlyInAnyOrder(FIRST_AUTHOR);
    }

    @DisplayName("add book author")
    @Test
    void shouldAddBookAuthor() {
        Book bookBefore = bookService.findById(EXISTING_BOOK_ID_2);
        assertThat(bookBefore.getAuthors()).extracting(Author::getName)
                .containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR);

        bookService.addAuthor(bookBefore, new Author(THIRD_AUTHOR));

        Book bookAfter = bookService.findById(EXISTING_BOOK_ID_2);
        assertThat(bookBefore.getAuthors()).extracting(Author::getName)
                .containsExactlyInAnyOrder(FIRST_AUTHOR, SECOND_AUTHOR, THIRD_AUTHOR);
    }

    @DisplayName("delete book from library")
    @Test
    void shouldCorrectlyDeleteBook() {
        Book bookBefore = bookService.findById(EXISTING_BOOK_ID_2);
        assertThat(bookBefore).isNotNull()
                .isInstanceOf(Book.class);

        bookService.delete(bookBefore);

        Book bookAfter = bookService.findById(EXISTING_BOOK_ID_2);
        assertThat(bookService.findById(EXISTING_BOOK_ID_2))
                .isNull();
    }
}
