package ru.otus.library.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.library.models.converters.BookMapper;
import ru.otus.library.models.domain.Book;
import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.models.dto.GenreDTO;
import ru.otus.library.repositories.BookRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, AuthorService authorService, GenreService genreService, CommentService commentService) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorService = authorService;
        this.genreService = genreService;
        this.commentService = commentService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAll() {
        return bookMapper.toBookDTOList(bookRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getById(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found in library."));
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        if(books.isEmpty()) {
            throw new NoSuchElementException("Books not found in library.");
        }
        return bookMapper.toBookDTOList(books);
    }

    @Override
    @Transactional
    public BookDTO add(BookDTO bookDTO) {
        for(int i = 0; i < bookDTO.getAuthors().size(); i++) {
            AuthorDTO authorDTO = bookDTO.getAuthors().get(i);
            bookDTO.getAuthors().set(i, authorService.getOrAdd(authorDTO.getName()));
        }
        for(int i = 0; i < bookDTO.getGenres().size(); i++) {
            GenreDTO genreDTO = bookDTO.getGenres().get(i);
            bookDTO.getGenres().set(i, genreService.getOrAdd(genreDTO.getName()));
        }
        Book book = bookRepository.save(bookMapper.toBook(bookDTO));
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO addAuthor(BookDTO bookDTO, AuthorDTO authorDTO) {
        authorDTO = authorService.getOrAdd(authorDTO.getName());
        if(!bookDTO.getAuthors().contains(authorDTO)) {
            bookDTO.getAuthors().add(authorDTO);
        }
        else {
            throw new RuntimeException("The book already has such an author.");
        }
        Book book = bookRepository.save(bookMapper.toBook(bookDTO));
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO addGenre(BookDTO bookDTO, GenreDTO genreDTO) {
        genreDTO = genreService.getOrAdd(genreDTO.getName());
        if(!bookDTO.getGenres().contains(genreDTO)) {
            bookDTO.getGenres().add(genreDTO);
        }
        else {
            throw new RuntimeException("The book already has such a genre.");
        }
        Book book = bookRepository.save(bookMapper.toBook(bookDTO));
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO update(BookDTO bookDTO) {
        if(bookRepository.findById(bookDTO.getId()).isEmpty()) {
            throw new RuntimeException("Book with such id is not presented in repository.");
        }
        for(int i = 0; i < bookDTO.getAuthors().size(); i++) {
            AuthorDTO authorDTO = bookDTO.getAuthors().get(i);
            bookDTO.getAuthors().set(i, authorService.getOrAdd(authorDTO.getName()));
        }
        for(int i = 0; i < bookDTO.getGenres().size(); i++) {
            GenreDTO genreDTO = bookDTO.getGenres().get(i);
            bookDTO.getGenres().set(i, genreService.getOrAdd(genreDTO.getName()));
        }
        bookRepository.save(bookMapper.toBook(bookDTO));
        return bookDTO;
    }

    @Override
    @Transactional
    public BookDTO changeTitle(BookDTO bookDTO, String newTitle) {
        Book book = bookRepository.findById(bookDTO.getId()).orElseThrow();
        book.setTitle(newTitle);
        book = bookRepository.save(book);
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO deleteAuthor(BookDTO bookDTO, AuthorDTO authorDTO) {
        if(bookDTO.getAuthors().contains(authorDTO)) {
            bookDTO.getAuthors().remove(authorDTO);
        }
        else {
            throw new RuntimeException("The book does not have such an author.");
        }
        Book book = bookRepository.save(bookMapper.toBook(bookDTO));
        return bookMapper.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO deleteGenre(BookDTO bookDTO, GenreDTO genreDTO) {
        if(bookDTO.getGenres().contains(genreDTO)) {
            bookDTO.getGenres().remove(genreDTO);
        }
        else {
            throw new RuntimeException("The book does not have such a genre.");
        }
        Book book = bookRepository.save(bookMapper.toBook(bookDTO));
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
