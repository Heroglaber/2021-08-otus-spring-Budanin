package ru.otus.library.models.converters;

import org.mapstruct.Mapper;
import ru.otus.library.models.domain.Author;
import ru.otus.library.models.dto.AuthorDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDTO toAuthorDTO(Author author);
    Author toAuthor(AuthorDTO authorDTO);
    List<AuthorDTO> toAuthorDTOList(List<Author> list);
    List<Author> toAuthorList(List<AuthorDTO> list);
}
