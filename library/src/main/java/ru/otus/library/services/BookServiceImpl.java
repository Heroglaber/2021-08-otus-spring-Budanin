package ru.otus.library.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.library.models.converters.AuthorMapper;
import ru.otus.library.models.converters.BookMapper;
import ru.otus.library.models.converters.GenreMapper;
import ru.otus.library.models.domain.Book;
import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.GenreDTO;
import ru.otus.library.repositories.BookRepository;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;

    public BookServiceImpl(BookRepository bookRepository, CommentService commentService, BookMapper bookMapper, AuthorMapper authorMapper, GenreMapper genreMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
        this.genreMapper = genreMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAll() {
        return bookMapper.toBookDTOList(bookRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getById(long id) {
        Book book = bookRepository.findById(id).orElseThrow();
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        return bookMapper.toBookDTOList(books);
    }

    @Override
    @Transactional
    public BookDTO add(BookDTO bookDTO) {
        Book book = bookMapper.toBook(bookDTO);
        bookRepository.save(book);
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO addAuthor(BookDTO bookDTO, AuthorDTO authorDTO) {
        Book book = bookMapper.toBook(bookDTO);
        book.getAuthors().add(authorMapper.toAuthor(authorDTO));
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO addGenre(BookDTO bookDTO, GenreDTO genreDTO) {
        Book book = bookMapper.toBook(bookDTO);
        book.getGenres().add(genreMapper.toGenre(genreDTO));
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO update(BookDTO bookDTO) {
        Book source = bookMapper.toBook(bookDTO);
        Book target = bookRepository.findById(bookDTO.getId())
                .orElseThrow();
        target.setTitle(source.getTitle());
        target.setAuthors(source.getAuthors());
        target.setGenres(source.getGenres());
        bookRepository.save(target);
        return bookMapper.toBookDTO(target);
    }

    @Override
    @Transactional
    public BookDTO changeTitle(BookDTO bookDTO, String newTitle) {
        Book book = bookMapper.toBook(bookDTO);
        book.setTitle(newTitle);
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO deleteAuthor(BookDTO bookDTO, AuthorDTO authorDTO) {
        Book book = bookMapper.toBook(bookDTO);
        book.getAuthors().remove(authorMapper.toAuthor(authorDTO));
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO deleteGenre(BookDTO bookDTO, GenreDTO genreDTO) {
        Book book = bookMapper.toBook(bookDTO);
        book.getGenres().remove(genreMapper.toGenre(genreDTO));
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO deleteById(long id) {
        Book book = bookRepository.findById(id).orElseThrow();
        bookRepository.deleteById(id);
        return bookMapper.toBookDTO(book);
    }
}
