package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.models.Author;
import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("BookService should ")
public class BookServiceIntegrationTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private BookServiceImpl bookService;

    @Test
    @DisplayName("correctly save book")
    @Transactional
    public void shouldAddBook() {
        final long BOOK_ID = 12;
        final String BOOK_TITLE = "The book Title";
        Book newBook = new Book(BOOK_ID, BOOK_TITLE,
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        assertThat(em.find(Book.class, BOOK_ID)).isNull();

        bookService.addBook(newBook);

        assertThat(em.find(Book.class, BOOK_ID)).isEqualTo(newBook)
            .matches(b -> b.getTitle().equals(BOOK_TITLE))
            .matches(b -> b.getComments() != null && b.getComments().size() == 0)
            .matches(b -> b.getAuthors() != null && b.getAuthors().size() == 0)
            .matches(b -> b.getGenres() != null && b.getGenres().size() == 0);
    }

    @Test
    @DisplayName("correctly save book with comments")
    @Transactional
    public void shouldAddBookWithComments() {
        final String BOOK_TITLE = "The book Title";
        final String COMMENT1 = "Great book! Love it.";
        final String COMMENT2 = "I read this book long ago";
        Comment comment1 = new Comment(COMMENT1);
        Comment comment2 = new Comment(COMMENT2);
        Book newBook = new Book(BOOK_TITLE);
        newBook.setComments(List.of(comment1, comment2));
        assertThat(em.find(Book.class, newBook.getId())).isNull();

        bookService.addBook(newBook);

        assertThat(em.find(Book.class, newBook.getId())).isEqualTo(newBook)
                .matches(b -> b.getTitle().equals(BOOK_TITLE), "has expected title")
                .matches(b -> b.getComments() != null && b.getComments().size() == 2, "has expected comments")
                .matches(b -> b.getAuthors() == null, "has expected authors")
                .matches(b -> b.getGenres() == null, "has expected genres");
    }

    @Test
    @DisplayName("correctly save book with authors")
    @Transactional
    public void shouldAddBookWithAuthors() {
        final String BOOK_TITLE = "The book Title";
        final String AUTHOR1 = "First Author";
        final String AUTHOR2 = "Second Author";
        Author author1 = new Author(AUTHOR1);
        Author author2 = new Author(AUTHOR2);
        Book newBook = new Book(BOOK_TITLE);
        newBook.setAuthors(List.of(author1, author2));
        assertThat(em.find(Book.class, newBook.getId())).isNull();

        bookService.addBook(newBook);

        assertThat(em.find(Book.class, newBook.getId())).isEqualTo(newBook)
                .matches(b -> b.getTitle().equals(BOOK_TITLE), "has expected title")
                .matches(b -> b.getComments() == null, "has expected comments")
                .matches(b -> b.getAuthors() != null && b.getAuthors().size() == 2, "has expected authors")
                .matches(b -> b.getGenres() == null, "has expected genres");
    }

    @Test
    @DisplayName("correctly save book with Genres")
    @Transactional
    public void shouldAddBookWithGenres() {
        final String BOOK_TITLE = "The book Title";
        final String GENRE1 = "First Genre";
        final String GENRE2 = "Second Genre";
        Genre genre1 = new Genre(GENRE1);
        Genre genre2 = new Genre(GENRE2);
        Book newBook = new Book(BOOK_TITLE);
        newBook.setGenres(List.of(genre1, genre2));
        assertThat(em.find(Book.class, newBook.getId())).isNull();

        bookService.addBook(newBook);

        assertThat(em.find(Book.class, newBook.getId())).isEqualTo(newBook)
                .matches(b -> b.getTitle().equals(BOOK_TITLE), "has expected title")
                .matches(b -> b.getComments() == null, "has expected comments")
                .matches(b -> b.getAuthors() == null, "has expected authors")
                .matches(b -> b.getGenres() != null && b.getGenres().size() == 2, "has expected genres");
    }

    @Test
    @DisplayName("correctly save book with all info")
    @Transactional
    public void shouldAddBookWithAllRelations() {
        final String BOOK_TITLE = "The book Title";
        final String GENRE1 = "First Genre";
        final String GENRE2 = "Second Genre";
        final String COMMENT1 = "Great book! Love it.";
        final String COMMENT2 = "I read this book long ago";
        final String AUTHOR1 = "First Author";
        final String AUTHOR2 = "Second Author";
        Author author1 = new Author(AUTHOR1);
        Author author2 = new Author(AUTHOR2);
        Comment comment1 = new Comment(COMMENT1);
        Comment comment2 = new Comment(COMMENT2);
        Genre genre1 = new Genre(GENRE1);
        Genre genre2 = new Genre(GENRE2);
        Book newBook = new Book(BOOK_TITLE);
        newBook.setComments(List.of(comment1, comment2));
        newBook.setAuthors(List.of(author1, author2));
        newBook.setGenres(List.of(genre1, genre2));
        assertThat(em.find(Book.class, newBook.getId())).isNull();

        bookService.addBook(newBook);

        assertThat(em.find(Book.class, newBook.getId())).isEqualTo(newBook)
                .matches(b -> b.getTitle().equals(BOOK_TITLE), "has expected title")
                .matches(b -> b.getComments() != null &&
                        b.getComments().containsAll(List.of(comment1, comment2)), "has expected comments")
                .matches(b -> b.getAuthors() != null &&
                        b.getAuthors().containsAll(List.of(author1, author2)), "has expected authors")
                .matches(b -> b.getGenres() != null &&
                        b.getGenres().containsAll(List.of(genre1, genre2)), "has expected genres");
    }

    @Test
    @DisplayName("correctly return all books")
    @Transactional
    public void shouldReturnAllBooks() {
        final int EXPECTED_BOOKS_AMOUNT = 10;
        assertThat(bookService.getAllBooks().size()).isEqualTo(EXPECTED_BOOKS_AMOUNT);
        assertThat(bookService.getAllBooks()).
                allMatch(book -> book.getTitle() != null);
    }

    @Test
    @DisplayName("should find book by title")
    @Transactional
    public void shouldSearchBookByTitle() {
        final String BOOK_TITLE = "Carrie";
        final String EXPECTED_AUTHOR = "Stephen King";
        final String EXPECTED_GENRE1 = "horror";
        final String EXPECTED_GENRE2 = "novel";
        assertThat(bookService.findBookByTitle(BOOK_TITLE)).isNotNull()
                .matches(book -> book.getAuthors().get(0).getName().equals(EXPECTED_AUTHOR))
                .matches(book -> book.getGenres().stream()
                        .map(Genre::getName)
                        .collect(Collectors.toList()).containsAll(List.of(EXPECTED_GENRE1, EXPECTED_GENRE2)));
    }

    @Test
    @DisplayName("should find book by id")
    @Transactional
    public void shouldSearchBookById() {
        final long BOOK_ID = 8;
        final String BOOK_TITLE = "Carrie";
        final String EXPECTED_AUTHOR = "Stephen King";
        final String EXPECTED_GENRE1 = "horror";
        final String EXPECTED_GENRE2 = "novel";
        assertThat(bookService.findBookById(BOOK_ID)).isNotNull()
                .matches(book -> book.getTitle().equals(BOOK_TITLE))
                .matches(book -> book.getAuthors().get(0).getName().equals(EXPECTED_AUTHOR))
                .matches(book -> book.getGenres().stream()
                        .map(Genre::getName)
                        .collect(Collectors.toList()).containsAll(List.of(EXPECTED_GENRE1, EXPECTED_GENRE2)));
    }

    @Test
    @DisplayName("should add author to book")
    @Transactional
    public void shouldAddBookAuthor() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";
        final String NEW_AUTHOR_NAME = "Author Name";
        Book bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);
        assertThat(bookBeforeUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2);

        Author newAuthor = new Author(NEW_AUTHOR_NAME);
        bookService.addBookAuthor(bookBeforeUpdate, newAuthor);
        assertThat(bookService.findBookByTitle(BOOK_TITLE).getAuthors())
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2, NEW_AUTHOR_NAME)
                .hasSize(EXPECTED_SIZE + 1);
    }

    @Test
    @DisplayName("should add genre to book")
    @Transactional
    public void shouldAddBookGenre() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";
        final String NEW_GENRE = "science non-fiction";
        Book bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);
        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(EXPECTED_SIZE)
                .extracting(Genre::getName).containsExactly(EXPECTED_GENRE);

        Genre newGenre = new Genre(NEW_GENRE);
        bookService.addBookGenre(bookBeforeUpdate, newGenre);
        assertThat(bookService.findBookByTitle(BOOK_TITLE).getGenres())
                .extracting(Genre::getName).containsExactly(EXPECTED_GENRE, NEW_GENRE)
                .hasSize(EXPECTED_SIZE + 1);
    }

    @Test
    @DisplayName("should add comment to book")
    @Transactional
    public void shouldAddBookComment() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String NEW_COMMENT = "I `m going to read this book";
        Book bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);
        assertThat(bookBeforeUpdate.getComments())
                .hasSize(EXPECTED_SIZE)
                .extracting(Comment::getMessage).doesNotContain(NEW_COMMENT);

        Comment newComment = new Comment(NEW_COMMENT);
        bookService.addBookComment(bookBeforeUpdate, newComment);
        assertThat(bookService.findBookByTitle(BOOK_TITLE).getComments())
                .extracting(Comment::getMessage).contains(NEW_COMMENT)
                .hasSize(EXPECTED_SIZE + 1);
    }

    @Test
    @DisplayName("should delete author from book")
    @Transactional
    public void shouldDeleteBookAuthor() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";
        Book bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);
        assertThat(bookBeforeUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2);

        Author existingAuthor = bookBeforeUpdate.getAuthors().get(0);
        bookService.deleteBookAuthor(bookBeforeUpdate, existingAuthor);
        assertThat(bookService.findBookByTitle(BOOK_TITLE).getAuthors())
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR2)
                .doesNotContain(EXPECTED_AUTHOR1)
                .hasSize(EXPECTED_SIZE - 1);
    }

    @Test
    @DisplayName("should delete book genre")
    @Transactional
    public void shouldDeleteBookGenre() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";
        Book bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);
        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(EXPECTED_SIZE)
                .extracting(Genre::getName).containsExactly(EXPECTED_GENRE);

        bookService.deleteBookGenre(bookBeforeUpdate, bookBeforeUpdate.getGenres().get(0));
        assertThat(bookService.findBookByTitle(BOOK_TITLE).getGenres())
                .isEmpty();
    }

    @Test
    @DisplayName("should delete book comment")
    @Transactional
    public void shouldDeleteBookComment() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_COMMENT1 = "Greatest book ever!";
        final String EXPECTED_COMMENT2 = "I read this book as a child.";
        Book bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);
        assertThat(bookBeforeUpdate.getComments())
                .hasSize(EXPECTED_SIZE)
                .extracting(Comment::getMessage).containsExactly(EXPECTED_COMMENT1, EXPECTED_COMMENT2);

        bookService.deleteBookComment(bookBeforeUpdate, bookBeforeUpdate.getComments().get(0));
        assertThat(bookService.findBookByTitle(BOOK_TITLE).getComments())
                .extracting(Comment::getMessage).contains(EXPECTED_COMMENT2)
                .doesNotContain(EXPECTED_COMMENT1)
                .hasSize(EXPECTED_SIZE - 1);
    }

    @DisplayName("should delete book by id")
    @Test
    @Transactional
    void shouldDeleteBookFromLibrary() {
        final long BOOK_ID = 5;
        final String EXPECTED_BOOK_TITLE = "Beloved";
        Book book = em.find(Book.class, BOOK_ID);
        assertThat(book).isNotNull()
                .extracting(Book::getTitle).isEqualTo(EXPECTED_BOOK_TITLE);

        bookService.deleteBookById(BOOK_ID);

        assertThat(em.find(Book.class, BOOK_ID)).isNull();
    }

    @DisplayName("should correctly change book title")
    @Test
    @Transactional
    void shouldChangeBookTitle() {
        final long BOOK_ID = 8;
        final String BOOK_TITLE = "Carrie";
        final String NEW_TITLE = "New Title";

        Book bookBeforeUpdate = em.find(Book.class, BOOK_ID);
        assertThat(bookBeforeUpdate.getTitle()).isEqualTo(BOOK_TITLE);

        em.detach(bookBeforeUpdate);
        bookService.changeBookTitle(bookBeforeUpdate, NEW_TITLE);

        Book bookAfterUpdate = em.find(Book.class, BOOK_ID);
        assertThat(bookAfterUpdate.getTitle()).isEqualTo(NEW_TITLE);
    }
}
