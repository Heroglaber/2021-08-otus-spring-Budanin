package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.CommentDTO;
import ru.otus.library.services.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = {"/comments"})
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public String getAll(@ModelAttribute("book") BookDTO book, Model model) {
        List<CommentDTO> comments = commentService.getAllByBookId(book.getId());
        model.addAttribute("comments", comments);
        return "comments_list";
    }

    @PostMapping("/add")
    String addComment(@ModelAttribute("book") BookDTO book,
            @ModelAttribute("newComment") String message) {
        CommentDTO comment = new CommentDTO(book, message);
        commentService.add(comment);
        return "redirect:/comments_list";
    }

    @DeleteMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") String commentId) {
        commentService.delete(commentId);
        return "redirect:/comments_list";
    }
}
