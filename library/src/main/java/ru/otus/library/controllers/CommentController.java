package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.CommentDTO;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = {"/comments"})
public class CommentController {
    private final CommentService commentService;
    private final BookService bookService;

    @GetMapping(value="/{bookId}")
    public String getAll(@PathVariable("bookId") String bookId,
                         Model model) {
        BookDTO book = bookService.getById(bookId);
        model.addAttribute("book", book);
        List<CommentDTO> comments;
        try {
            comments = commentService.getAllByBookId(bookId);
        }
        catch(RuntimeException e) {
            comments = new ArrayList<>();
        }
        model.addAttribute("comments", comments);
        model.addAttribute("newComment", new CommentDTO(book, ""));
        return "comments_list";
    }

    @PostMapping("/add/{bookId}")
    String addComment(@PathVariable("bookId") String bookId,
            @ModelAttribute("newComment") CommentDTO comment) {
        BookDTO book = bookService.getById(bookId);
        comment.setBook(book);
        commentService.add(comment);
        return "redirect:/comments/" + bookId;
    }

    @DeleteMapping(value="/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") String commentId) {
        String bookId = commentService.get(commentId).getBook().getId();
        commentService.delete(commentId);
        return "redirect:/comments/" + bookId;
    }
}
