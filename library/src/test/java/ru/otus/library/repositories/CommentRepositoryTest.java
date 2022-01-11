package ru.otus.library.repositories;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.library.configs.MongoConfig;
import ru.otus.library.models.converters.AuthorMapperImpl;
import ru.otus.library.models.converters.BookMapperImpl;
import ru.otus.library.models.converters.CommentMapperImpl;
import ru.otus.library.models.converters.GenreMapperImpl;
import ru.otus.library.models.domain.Book;
import ru.otus.library.models.domain.Comment;
import ru.otus.library.services.AuthorServiceImpl;
import ru.otus.library.services.BookServiceImpl;
import ru.otus.library.services.CommentServiceImpl;
import ru.otus.library.services.GenreServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Comment repository should ")
@DataMongoTest
@Import({ MongoConfig.class, BookServiceImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, CommentMapperImpl.class, GenreMapperImpl.class, AuthorServiceImpl.class, GenreServiceImpl.class, CommentServiceImpl.class})
public class CommentRepositoryTest {
    private static final String EXISTING_BOOK_TITLE = "Carrie";
    private static final String EXISTING_COMMENT_MESSAGE = "Scary book!";
    private static final int COMMENTS_TABLE_SIZE = 4;
    private static final int BOOK_COMMENTS_TABLE_SIZE = 2;
    private static final String EXISTING_COMMENT_MESSAGE_2 = "JUST AMAZING";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("correctly save comment")
    @Test
    void shouldSaveComment() {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(EXISTING_BOOK_TITLE));
        Book book = mongoTemplate.find(query, Book.class).get(0);

        String message = "Test Comment";
        Comment expectedComment = new Comment(book, message);

        commentRepository.save(expectedComment);

        assertThat(expectedComment.getId()).isNotBlank();
        Comment actualComment = commentRepository.findById(expectedComment.getId()).orElseThrow();
        assertThat(expectedComment).usingRecursiveComparison().ignoringAllOverriddenEquals()
                .isEqualTo(actualComment);
    }

    @DisplayName("find comment by id")
    @Test
    void shouldFindCommentById() {
        Query query = new Query();
        query.addCriteria(Criteria.where("message").is(EXISTING_COMMENT_MESSAGE));
        Comment expectedComment = mongoTemplate.find(query, Comment.class).get(0);

        Comment actualComment = commentRepository.findById(expectedComment.getId()).orElseThrow();

        assertThat(expectedComment).usingRecursiveComparison().ignoringAllOverriddenEquals()
                .isEqualTo(actualComment);
    }

    @DisplayName("find comment by message")
    @Test
    void shouldFindCommentByMessage() {
        Query query = new Query();
        query.addCriteria(Criteria.where("message").is(EXISTING_COMMENT_MESSAGE));
        Comment expectedComment = mongoTemplate.find(query, Comment.class).get(0);

        Comment actualComment = commentRepository.findByMessage(expectedComment.getMessage()).orElseThrow();

        assertThat(expectedComment).usingRecursiveComparison().ignoringAllOverriddenEquals()
                .isEqualTo(actualComment);
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
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(EXISTING_BOOK_TITLE));
        Book book = mongoTemplate.find(query, Book.class).get(0);

        List<Comment> comments = commentRepository.findAllByBook_Id(book.getId());

        assertThat(comments.size()).isEqualTo(BOOK_COMMENTS_TABLE_SIZE);
        assertThat(comments).extracting("message")
                .containsExactlyInAnyOrder(EXISTING_COMMENT_MESSAGE, EXISTING_COMMENT_MESSAGE_2);
    }

    @DisplayName("delete comment by id")
    @Test
    void deleteById(){
        Query query = new Query();
        query.addCriteria(Criteria.where("message").is(EXISTING_COMMENT_MESSAGE_2));
        Comment expectedComment = mongoTemplate.find(query, Comment.class).get(0);
        String id = expectedComment.getId();

        Comment deletedComment = mongoTemplate.findById(id, Comment.class);
        AssertionsForClassTypes.assertThat(deletedComment).extracting("message")
                .isEqualTo(EXISTING_COMMENT_MESSAGE_2);

        commentRepository.deleteById(id);
        AssertionsForClassTypes.assertThat(mongoTemplate.findById(id, Comment.class)).isNull();
    }
}
