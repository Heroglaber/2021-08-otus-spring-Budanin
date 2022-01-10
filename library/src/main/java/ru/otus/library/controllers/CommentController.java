package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.models.dto.CommentDTO;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final BookService bookService;
    private final CommentService commentService;

    @GetMapping("/comment")
    public List<CommentDTO> getAll() {
        try {
            return commentService.getAll();
        }
        catch(NoSuchElementException e) {
            return null;
        }
    }

    @GetMapping("/book/{bookId}/comment")
    public List<CommentDTO> getAllByBook(@PathVariable("bookId") String bookId) {
        try {
            return commentService.getAllByBookId(bookId);
        }
        catch(NoSuchElementException e) {
            return null;
        }
    }

    @GetMapping("/comment/{commentId}")
    public CommentDTO getBook(@PathVariable("commentId") String commentId) {
        try {
            return commentService.get(commentId);
        }
        catch(NoSuchElementException e) {
            return null;
        }
    }

    @PostMapping("/book/{bookId}/comment")
    public CommentDTO addComment(@RequestBody CommentDTO comment, @PathVariable("bookId") String bookId) {
        comment.setBook(bookService.getById(bookId));
        return commentService.add(comment);
    }

    @PostMapping("/book/{bookId}/comment/{commentId}")
    public CommentDTO updateCommentMessage(@RequestBody CommentDTO comment
                                           ,@PathVariable("bookId") String bookId
                                           ,@PathVariable("commentId") String commentId) {
        comment.setId(commentId);
        comment.setBook(bookService.getById(bookId));
        return commentService.update(comment);
    }

    @DeleteMapping(value="/comment/{commentId}")
    public CommentDTO deleteComment(@PathVariable("commentId") String commentId) {
        try {
            return commentService.delete(commentId);
        }
        catch(NoSuchElementException e) {
            return null;
        }
    }
}
