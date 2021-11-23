package ru.otus.library.models.converters;

import org.mapstruct.Mapper;
import ru.otus.library.models.domain.Genre;
import ru.otus.library.models.dto.GenreDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDTO toGenreDTO(Genre genre);
    Genre toGenre(GenreDTO genreDTO);
    List<GenreDTO> toGenreDTOList(List<Genre> list);
    List<Genre> toGenreList(List<GenreDTO> list);
}
