package ru.otus.library.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.CommentDTO;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @DisplayName("correctly return all comments")
    @Test
    public void shouldReturnAllBooks() throws Exception {
        BookDTO book = new BookDTO("Book Title");
        List<CommentDTO> comments = List.of(new CommentDTO(book, "comment message"));

        when(commentService.getAll()).thenReturn(comments);

        mvc.perform(get("/comment")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is("comment message")))
                .andExpect(jsonPath("$[0].book.title", is("Book Title")));
    }

    @DisplayName("return comments by book id")
    @Test
    public void shouldReturnAllCommentsByBookId() throws Exception {
        String id = "61b0a643595bdd0bf6720038";
        BookDTO book = new BookDTO("Book Title");
        book.setId(id);
        List<CommentDTO> comments = List.of(new CommentDTO(book, "comment message"));

        when(commentService.getAllByBookId(id)).thenReturn(comments);

        mvc.perform(get("/book/{bookId}/comment", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is("comment message")))
                .andExpect(jsonPath("$[0].book.id", is(id)));
    }

    @DisplayName("return comment by id")
    @Test
    public void shouldReturnCommentById() throws Exception {
        String commentId = "61b0a643595bdd0bf6720038";
        BookDTO book = new BookDTO("Book Title");
        CommentDTO comment = new CommentDTO(book, "comment message");
        comment.setId(commentId);

        when(commentService.get(commentId)).thenReturn(comment);

        mvc.perform(get("/comment/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("comment message")))
                .andExpect(jsonPath("$.id", is(commentId)));
    }

    @DisplayName("add new comment")
    @Test
    public void shouldAddNewComment() throws Exception {
        String bookId = "123456789";
        String commentId = "61b0a643595bdd0bf6720038";
        BookDTO book = new BookDTO("Book Title");
        book.setId(bookId);
        CommentDTO comment = new CommentDTO(book, "comment message");
        comment.setId(commentId);
        String json = "{ \"message\": \"comment message\" }";

        when(bookService.getById(bookId)).thenReturn(book);
        when(commentService.add(comment)).thenReturn(comment);

        mvc.perform(post("/book/{bookId}/comment", bookId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentId)))
                .andExpect(jsonPath("$.message", is("comment message")));

    }

    @DisplayName("update comment message")
    @Test
    public void shouldUpdateComment() throws Exception {
        String bookId = "123456789";
        String commentId = "61b0a643595bdd0bf6720038";
        BookDTO book = new BookDTO("Book Title");
        book.setId(bookId);
        CommentDTO comment = new CommentDTO(book, "old message");
        comment.setId(commentId);
        String json = "{ \"message\": \"new message\" }";

        when(bookService.getById(bookId)).thenReturn(book);
        CommentDTO updatedComment = new CommentDTO(book, "new message");
        updatedComment.setId(commentId);
        when(commentService.update(any())).thenReturn(updatedComment);

        mvc.perform(post("/book/{bookId}/comment/{commentId}", bookId, commentId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentId)))
                .andExpect(jsonPath("$.message", is("new message")));

    }

    @DisplayName("delete comment")
    @Test
    public void shouldDeleteComment() throws Exception {
        String bookId = "123456789";
        String commentId = "61b0a643595bdd0bf6720038";
        BookDTO book = new BookDTO("Book Title");
        book.setId(bookId);
        CommentDTO comment = new CommentDTO(book, "comment message");
        comment.setId(commentId);

        when(commentService.delete(commentId)).thenReturn(comment);

        mvc.perform(delete("/comment/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentId)))
                .andExpect(jsonPath("$.message", is("comment message")));

    }
}
