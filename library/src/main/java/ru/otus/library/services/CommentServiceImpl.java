package ru.otus.library.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.models.converters.CommentMapper;
import ru.otus.library.models.domain.Comment;
import ru.otus.library.models.dto.CommentDTO;
import ru.otus.library.repositories.CommentRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getAll() {
        return commentMapper.toCommentDTOList(commentRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getAllByBookId(String bookId) {
        List<Comment> comments = commentRepository.findAllByBook_Id(bookId);
        if(comments.isEmpty()) {
            throw new NoSuchElementException("Comments not found for this book.");
        }
        return commentMapper.toCommentDTOList(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDTO get(String id) {
        Comment comment = commentRepository.findById(id)
                        .orElseThrow();
        return commentMapper.toCommentDTO(comment);
    }

    @Override
    @Transactional
    public CommentDTO add(CommentDTO commentDTO) {
        Comment comment = commentMapper.toComment(commentDTO);
        comment = commentRepository.save(comment);
        return commentMapper.toCommentDTO(comment);
    }

    @Override
    @Transactional
    public List<CommentDTO> addAll(List<CommentDTO> commentsDTOs) {
        List<Comment> comments = commentMapper.toCommentList(commentsDTOs);
        comments = commentRepository.saveAll(comments);
        return commentMapper.toCommentDTOList(comments);
    }

    @Override
    @Transactional
    public CommentDTO update(CommentDTO commentDTO) {
        Comment source = commentMapper.toComment(commentDTO);
        Comment target = commentRepository.findById(commentDTO.getId())
                .orElseThrow();
        target.setBook(source.getBook());
        target.setMessage(source.getMessage());
        target = commentRepository.save(target);
        return commentMapper.toCommentDTO(target);
    }

    @Override
    @Transactional
    public CommentDTO delete(String id) {
        CommentDTO commentDTO = get(id);
        commentRepository.deleteById(id);
        return commentDTO;
    }

    @Override
    @Transactional
    public void deleteAllByBookId(String bookId) {
        commentRepository.deleteAllByBook_Id(bookId);
    }
}
