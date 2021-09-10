package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dto.BookDto;
import ru.otus.library.models.Author;
import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;
import ru.otus.library.models.Genre;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

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
        final String BOOK_TITLE = "The New Title";
        final String FIRST_AUTHOR = "First Author";
        final String SECOND_AUTHOR = "Second Author";
        BookDto newBook = new BookDto();
        newBook.setTitle(BOOK_TITLE);
        newBook.setAuthors(List.of(new Author(FIRST_AUTHOR), new Author(SECOND_AUTHOR)));
        assertThat(bookService.getAllBooks()).hasSize(10)
                .doesNotContain(newBook);

        bookService.addBook(newBook);

        assertThat(bookService.findBookByTitle(BOOK_TITLE).getAuthors()).isEqualTo(newBook.getAuthors());

        assertThat(bookService.getAllBooks()).hasSize(11)
                .contains(newBook);
    }

    @Test
    @DisplayName("correctly save book with comments")
    @Transactional
    public void shouldAddBookWithComments() {
        assertThat(bookService.getAllBooks()).hasSize(10);
        final String BOOK_TITLE = "The book Title";
        final String COMMENT1 = "Great book! Love it.";
        final String COMMENT2 = "I read this book long ago";
        BookDto newBook = new BookDto();
        newBook.setTitle(BOOK_TITLE);
        newBook.setComments(List.of(new Comment(COMMENT1), new Comment(COMMENT2)));

        assertThat(bookService.getAllBooks()).hasSize(10)
                .doesNotContain(newBook);

        var insertedBook = bookService.addBook(newBook);

        assertThat(bookService.getAllBooks()).hasSize(11)
                .contains(newBook);
        assertThat(insertedBook.getComments()).hasSize(2)
                .extracting(Comment::getMessage).containsExactlyInAnyOrder(COMMENT1, COMMENT2);
        assertThat(em.find(Comment.class, insertedBook.getComments().get(0).getId())).isNotNull()
                .isEqualTo(newBook.getComments().get(0));
        assertThat(em.find(Comment.class, insertedBook.getComments().get(1).getId())).isNotNull()
                .isEqualTo(newBook.getComments().get(1));
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
        BookDto newBook = new BookDto();
        newBook.setTitle(BOOK_TITLE);
        newBook.setAuthors(List.of(author1, author2));

        assertThat(bookService.getAllBooks()).hasSize(10)
                .doesNotContain(newBook);

        var insertedBook = bookService.addBook(newBook);

        assertThat(bookService.getAllBooks()).hasSize(11)
                .contains(newBook);
        assertThat(insertedBook.getAuthors()).hasSize(2)
                .extracting(Author::getName).containsExactlyInAnyOrder(AUTHOR1, AUTHOR2);
        assertThat(em.find(Author.class, insertedBook.getAuthors().get(0).getId())).isNotNull()
                .isEqualTo(newBook.getAuthors().get(0));
        assertThat(em.find(Author.class, insertedBook.getAuthors().get(1).getId())).isNotNull()
                .isEqualTo(newBook.getAuthors().get(1));
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
        BookDto newBook = new BookDto();
        newBook.setTitle(BOOK_TITLE);
        newBook.setGenres(List.of(genre1, genre2));

        assertThat(bookService.getAllBooks()).hasSize(10)
                .doesNotContain(newBook);

        var insertedBook = bookService.addBook(newBook);

        assertThat(bookService.getAllBooks()).hasSize(11)
                .contains(newBook);
        assertThat(insertedBook.getGenres()).hasSize(2)
                .extracting(Genre::getName).containsExactlyInAnyOrder(GENRE1, GENRE2);
        assertThat(em.find(Genre.class, insertedBook.getGenres().get(0).getId())).isNotNull()
                .isEqualTo(newBook.getGenres().get(0));
        assertThat(em.find(Genre.class, insertedBook.getGenres().get(1).getId())).isNotNull()
                .isEqualTo(newBook.getGenres().get(1));
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
        BookDto newBook = new BookDto();
        newBook.setTitle(BOOK_TITLE);
        newBook.setComments(List.of(comment1, comment2));
        newBook.setAuthors(List.of(author1, author2));
        newBook.setGenres(List.of(genre1, genre2));

        assertThat(bookService.getAllBooks()).hasSize(10)
                .doesNotContain(newBook);

        var insertedBook = bookService.addBook(newBook);

        assertThat(bookService.getAllBooks()).hasSize(11)
                .contains(newBook);
        assertThat(insertedBook.getComments()).hasSize(2)
                .extracting(Comment::getMessage).containsExactlyInAnyOrder(COMMENT1, COMMENT2);
        assertThat(em.find(Comment.class, insertedBook.getComments().get(0).getId())).isNotNull()
                .isEqualTo(newBook.getComments().get(0));
        assertThat(em.find(Comment.class, insertedBook.getComments().get(1).getId())).isNotNull()
                .isEqualTo(newBook.getComments().get(1));
        assertThat(insertedBook.getAuthors()).hasSize(2)
                .extracting(Author::getName).containsExactlyInAnyOrder(AUTHOR1, AUTHOR2);
        assertThat(em.find(Author.class, insertedBook.getAuthors().get(0).getId())).isNotNull()
                .isEqualTo(newBook.getAuthors().get(0));
        assertThat(em.find(Author.class, insertedBook.getAuthors().get(1).getId())).isNotNull()
                .isEqualTo(newBook.getAuthors().get(1));
        assertThat(insertedBook.getGenres()).hasSize(2)
                .extracting(Genre::getName).containsExactlyInAnyOrder(GENRE1, GENRE2);
        assertThat(em.find(Genre.class, insertedBook.getGenres().get(0).getId())).isNotNull()
                .isEqualTo(newBook.getGenres().get(0));
        assertThat(em.find(Genre.class, insertedBook.getGenres().get(1).getId())).isNotNull()
                .isEqualTo(newBook.getGenres().get(1));
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
        final String EXPECTED_COMMENT1 = "Scary book!";
        final String EXPECTED_COMMENT2 = "JUST AMAZING";

        var book = bookService.findBookByTitle(BOOK_TITLE);

        assertThat(book).isNotNull().isInstanceOf(BookDto.class);
        assertThat(book.getAuthors()).extracting(Author::getName)
                .containsExactlyInAnyOrder(EXPECTED_AUTHOR);
        assertThat(book.getGenres()).extracting(Genre::getName)
                .containsExactlyInAnyOrder(EXPECTED_GENRE1, EXPECTED_GENRE2);
        assertThat(book.getComments()).extracting(Comment::getMessage)
                .containsExactlyInAnyOrder(EXPECTED_COMMENT1, EXPECTED_COMMENT2);
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
        final String EXPECTED_COMMENT1 = "Scary book!";
        final String EXPECTED_COMMENT2 = "JUST AMAZING";

        var book = bookService.findBookById(BOOK_ID);

        assertThat(book).isNotNull().isInstanceOf(BookDto.class);
        assertThat(book.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(book.getAuthors()).extracting(Author::getName)
                .containsExactlyInAnyOrder(EXPECTED_AUTHOR);
        assertThat(book.getGenres()).extracting(Genre::getName)
                .containsExactlyInAnyOrder(EXPECTED_GENRE1, EXPECTED_GENRE2);
        assertThat(book.getComments()).extracting(Comment::getMessage)
                .containsExactlyInAnyOrder(EXPECTED_COMMENT1, EXPECTED_COMMENT2);
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

        BookDto bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);

        assertThat(bookBeforeUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2);

        Author newAuthor = new Author(NEW_AUTHOR_NAME);

        var bookAfterUpdate = bookService.addBookAuthor(bookBeforeUpdate, newAuthor);

        assertThat(bookAfterUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE + 1)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2, NEW_AUTHOR_NAME);
        assertThat(em.find(Book.class, bookAfterUpdate.getId()).getAuthors())
                .extracting(Author::getName).contains(NEW_AUTHOR_NAME);
        assertThat(em.find(Author.class, bookAfterUpdate.getAuthors().get(2).getId()))
                .isEqualTo(bookAfterUpdate.getAuthors().get(2));
    }

    @Test
    @DisplayName("should add genre to book")
    @Transactional
    public void shouldAddBookGenre() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";
        final String NEW_GENRE = "science non-fiction";

        BookDto bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);

        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(EXPECTED_SIZE)
                .extracting(Genre::getName).containsExactly(EXPECTED_GENRE);

        Genre newGenre = new Genre(NEW_GENRE);

        var bookAfterUpdate = bookService.addBookGenre(bookBeforeUpdate, newGenre);

        assertThat(bookAfterUpdate.getGenres())
                .hasSize(EXPECTED_SIZE + 1)
                .extracting(Genre::getName).containsExactly(EXPECTED_GENRE, NEW_GENRE);
        assertThat(em.find(Book.class, bookAfterUpdate.getId()).getGenres())
                .extracting(Genre::getName).contains(EXPECTED_GENRE, NEW_GENRE);
        assertThat(em.find(Genre.class, bookAfterUpdate.getGenres().get(1).getId()))
                .isEqualTo(bookAfterUpdate.getGenres().get(1));
    }

    @Test
    @DisplayName("should add comment to book")
    @Transactional
    public void shouldAddBookComment() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_COMMENT1 = "Greatest book ever!";
        final String EXPECTED_COMMENT2 = "I read this book as a child.";
        final String NEW_COMMENT = "I `m going to read this book";

        BookDto bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);

        assertThat(bookBeforeUpdate.getComments())
                .hasSize(EXPECTED_SIZE)
                .extracting(Comment::getMessage).containsExactlyInAnyOrder(EXPECTED_COMMENT1,
                EXPECTED_COMMENT2);

        Comment newComment = new Comment(NEW_COMMENT);

        var bookAfterUpdate = bookService.addBookComment(bookBeforeUpdate, newComment);

        assertThat(bookAfterUpdate.getComments())
                .hasSize(EXPECTED_SIZE + 1)
                .extracting(Comment::getMessage).containsExactlyInAnyOrder(EXPECTED_COMMENT1,
                EXPECTED_COMMENT2, NEW_COMMENT);
        long insertedBookId = bookAfterUpdate.getComments().stream()
                .filter(comment -> comment.getMessage().equals(NEW_COMMENT)).findFirst().get().getId();
        assertThat(em.find(Comment.class, insertedBookId))
                .extracting(Comment::getMessage).isEqualTo(NEW_COMMENT);
    }

    @Test
    @DisplayName("should delete author from book")
    @Transactional
    public void shouldDeleteBookAuthor() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_AUTHOR1 = "Arkady Strugatsky";
        final String EXPECTED_AUTHOR2 = "Boris Strugatsky";

        BookDto bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);

        assertThat(bookBeforeUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE)
                .extracting(Author::getName).containsExactlyInAnyOrder(EXPECTED_AUTHOR1,
                EXPECTED_AUTHOR2);

        Author deleted = bookBeforeUpdate.getAuthors().get(1);
        var bookAfterUpdate = bookService.deleteBookAuthor(bookBeforeUpdate, deleted);

        assertThat(bookAfterUpdate.getAuthors())
                .hasSize(EXPECTED_SIZE - 1)
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1);
        assertThat(em.find(Book.class, bookAfterUpdate.getId()).getAuthors())
                .extracting(Author::getName).containsExactly(EXPECTED_AUTHOR1);
    }

    @Test
    @DisplayName("should delete book genre")
    @Transactional
    public void shouldDeleteBookGenre() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 1;
        final String EXPECTED_GENRE = "fantasy";

        BookDto bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);

        assertThat(bookBeforeUpdate.getGenres())
                .hasSize(EXPECTED_SIZE)
                .extracting(Genre::getName).containsExactlyInAnyOrder(EXPECTED_GENRE);

        Genre deleted = bookBeforeUpdate.getGenres().get(0);
        var bookAfterUpdate = bookService.deleteBookGenre(bookBeforeUpdate, deleted);

        assertThat(bookAfterUpdate.getGenres())
                .isEmpty();
        assertThat(em.find(Book.class, bookAfterUpdate.getId()).getGenres())
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

        BookDto bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);

        assertThat(bookBeforeUpdate.getComments())
                .hasSize(EXPECTED_SIZE)
                .extracting(Comment::getMessage).containsExactlyInAnyOrder(EXPECTED_COMMENT1,
                EXPECTED_COMMENT2);

        Comment oldComment = bookBeforeUpdate.getComments().get(1);

        var bookAfterUpdate = bookService.deleteBookComment(bookBeforeUpdate, oldComment);

        assertThat(bookAfterUpdate.getComments())
                .hasSize(EXPECTED_SIZE - 1)
                .extracting(Comment::getMessage).containsExactlyInAnyOrder(EXPECTED_COMMENT1);
        assertThat(bookService.findBookByTitle(BOOK_TITLE).getComments())
                .hasSize(EXPECTED_SIZE - 1)
                .extracting(Comment::getMessage).containsExactlyInAnyOrder(EXPECTED_COMMENT1);
        assertThat(em.find(Comment.class, oldComment.getId()))
                .isNull();
    }

    @DisplayName("should delete book by id")
    @Test
    @Transactional
    void shouldDeleteBookFromLibrary() {
        final String BOOK_TITLE = "The Roadside Picnic";
        final int EXPECTED_SIZE = 2;
        final String EXPECTED_COMMENT1 = "Greatest book ever!";
        final String EXPECTED_COMMENT2 = "I read this book as a child.";

        BookDto bookBeforeUpdate = bookService.findBookByTitle(BOOK_TITLE);

        assertThat(em.find(Book.class, bookBeforeUpdate.getId())).isNotNull()
                .extracting(Book::getTitle).isEqualTo(BOOK_TITLE);
        assertThat(bookBeforeUpdate.getComments())
                .hasSize(EXPECTED_SIZE)
                .extracting(Comment::getMessage).containsExactlyInAnyOrder(EXPECTED_COMMENT1,
                EXPECTED_COMMENT2);

        Comment firstCommentBeforeDelete = bookBeforeUpdate.getComments().get(0);
        Comment secondCommentBeforeDelete = bookBeforeUpdate.getComments().get(1);

        assertThat(em.find(Comment.class, firstCommentBeforeDelete.getId()))
                .isNotNull();
        assertThat(em.find(Comment.class, secondCommentBeforeDelete.getId()))
                .isNotNull();

        bookService.deleteBookById(bookBeforeUpdate.getId());

        assertThat(em.find(Book.class, bookBeforeUpdate.getId())).isNull();
        assertThat(em.find(Comment.class, firstCommentBeforeDelete.getId())).isNull();
        assertThat(em.find(Comment.class, secondCommentBeforeDelete.getId())).isNull();
    }

    @DisplayName("should correctly change book title")
    @Test
    @Transactional
    void shouldChangeBookTitle() {
        final long BOOK_ID = 8;
        final String BOOK_TITLE = "Carrie";
        final String NEW_TITLE = "New Title";

        BookDto bookBeforeUpdate = bookService.findBookById(BOOK_ID);
        assertThat(bookBeforeUpdate.getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(em.find(Book.class, BOOK_ID).getTitle()).isEqualTo(BOOK_TITLE);

        bookService.changeBookTitle(bookBeforeUpdate, NEW_TITLE);

        BookDto bookAfterUpdate = bookService.findBookById(BOOK_ID);
        assertThat(bookAfterUpdate.getTitle()).isEqualTo(NEW_TITLE);
        assertThat(em.find(Book.class, BOOK_ID).getTitle()).isEqualTo(NEW_TITLE);
    }
}
