package ru.otus.library.models.converters;

import org.mapstruct.Mapper;
import ru.otus.library.models.domain.Comment;
import ru.otus.library.models.dto.CommentDTO;

import java.util.List;

@Mapper(uses = {BookMapper.class}, componentModel = "spring")
public interface CommentMapper {
    CommentDTO toCommentDTO(Comment comment);
    Comment toComment(CommentDTO commentDTO);
    List<CommentDTO> toCommentDTOList(List<Comment> list);
    List<Comment> toCommentList(List<CommentDTO> list);
}
