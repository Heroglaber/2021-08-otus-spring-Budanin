package ru.otus.library.services;

import ru.otus.library.models.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAll();
    List<CommentDTO> getAllByBookId(String bookId);
    CommentDTO get(String id);
    CommentDTO add(CommentDTO commentDTO);
    List<CommentDTO> addAll(List<CommentDTO> comments);
    CommentDTO update(CommentDTO commentDTO);
    CommentDTO delete(String id);
    void deleteAllByBookId(String bookId);
}
