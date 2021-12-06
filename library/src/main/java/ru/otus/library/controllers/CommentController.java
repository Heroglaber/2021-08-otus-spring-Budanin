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
        List<CommentDTO> comments = new ArrayList<>();
        try {
            comments = commentService.getAllByBookId(bookId);
        }
        catch(RuntimeException e) {
            comments = new ArrayList<>();
        }
        model.addAttribute("comments", comments);
        return "comments_list";
    }

    @PostMapping("/add")
    String addComment(@ModelAttribute("book") BookDTO book,
            @ModelAttribute("newComment") String message) {
        CommentDTO comment = new CommentDTO(book, message);
        commentService.add(comment);
        return "redirect:/comments/" + book.getId();
    }

    @DeleteMapping(value="/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") String commentId) {
        String bookId = commentService.get(commentId).getBook().getId();
        commentService.delete(commentId);
        return "redirect:/comments/" + bookId;
    }
}
