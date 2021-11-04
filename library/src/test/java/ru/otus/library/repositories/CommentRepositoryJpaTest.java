package ru.otus.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.library.models.domain.Book;
import ru.otus.library.models.domain.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Comment repository should ")
@DataJpaTest
public class CommentRepositoryJpaTest {
    private static final long EXISTING_COMMENT_ID = 3L;
    private static final String EXISING_COMMENT_MESSAGE = "Scary book!";
    private static final int COMMENTS_TABLE_SIZE = 4;
    private static final long EXISTING_BOOK_ID = 8L;
    private static final String EXISING_COMMENT_MESSAGE_2 = "JUST AMAZING";

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("correctly save comment")
    @Test
    void shouldSaveComment() {
        Book book = em.find(Book.class, EXISTING_BOOK_ID);
        String message = "Test Comment";
        Comment expectedComment = new Comment(book, message);

        commentRepository.save(expectedComment);

        assertThat(expectedComment.getId()).isGreaterThan(0);
        Comment actualComment = em.find(Comment.class, expectedComment.getId());
        assertThat(expectedComment).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualComment);
    }

    @DisplayName("find comment by id")
    @Test
    void shouldFindCommentById() {
        Book book = em.find(Book.class, EXISTING_BOOK_ID);
        Comment expectedComment = new Comment(book, EXISING_COMMENT_MESSAGE);
        expectedComment.setId(EXISTING_COMMENT_ID);

        Comment actualComment = commentRepository.findById(EXISTING_COMMENT_ID).orElseThrow();

        assertThat(expectedComment).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualComment);
    }

    @DisplayName("find comment by message")
    @Test
    void shouldFindCommentByMessage() {
        Book book = em.find(Book.class, EXISTING_BOOK_ID);
        Comment expectedComment = new Comment(book, EXISING_COMMENT_MESSAGE);
        expectedComment.setId(EXISTING_COMMENT_ID);

        Comment actualComment = commentRepository.findByMessage(EXISING_COMMENT_MESSAGE).orElseThrow();

        assertThat(expectedComment).usingRecursiveComparison().ignoringAllOverriddenEquals().isEqualTo(actualComment);
    }

    @DisplayName("find all comments")
    @Test
    void findAll() {
        List<Comment> comments = commentRepository.findAll();

        assertThat(comments.size()).isEqualTo(COMMENTS_TABLE_SIZE);
    }

    @DisplayName("find all comments by book id")
    @Test
    void findAllByBookId() {
        int expected_size = 2;
        List<Comment> comments = commentRepository.findAllByBook_Id(EXISTING_BOOK_ID);

        assertThat(comments.size()).isEqualTo(expected_size);
        assertThat(comments).extracting("message")
                .containsExactlyInAnyOrder(EXISING_COMMENT_MESSAGE, EXISING_COMMENT_MESSAGE_2);
    }

    @DisplayName("delete comment by id")
    @Test
    void deleteById(){
        Comment deletedComment = em.find(Comment.class, EXISTING_COMMENT_ID);
        assertThat(deletedComment).isNotNull().extracting("id", "message")
                .containsExactly(EXISTING_COMMENT_ID, EXISING_COMMENT_MESSAGE);

        commentRepository.deleteById(EXISTING_COMMENT_ID);

        assertThat(em.find(Comment.class, EXISTING_COMMENT_ID)).isNull();
    }
}
