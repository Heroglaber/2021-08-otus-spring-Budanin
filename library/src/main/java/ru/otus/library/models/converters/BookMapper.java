package ru.otus.library.models.converters;

import org.mapstruct.Mapper;
import ru.otus.library.models.domain.Book;
import ru.otus.library.models.dto.BookDTO;

import java.util.List;

@Mapper(uses = {AuthorMapper.class, GenreMapper.class}, componentModel = "spring")
public interface BookMapper {
    BookDTO toBookDTO(Book book);
    Book toBook(BookDTO bookDTO);
    List<BookDTO> toBookDTOList(List<Book> list);
    List<Book> toBookList(List<BookDTO> list);
}
