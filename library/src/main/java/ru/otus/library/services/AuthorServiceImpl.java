package ru.otus.library.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.models.converters.AuthorMapper;
import ru.otus.library.models.converters.BookMapper;
import ru.otus.library.models.domain.Author;
import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.models.dto.BookDTO;
import ru.otus.library.repositories.AuthorRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper, BookMapper bookMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.bookMapper = bookMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> getAll() {
        return authorMapper.toAuthorDTOList(authorRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDTO get(AuthorDTO authorDTO) {
        Author author = authorRepository.findByName(authorDTO.getName()).orElseThrow();
        return authorMapper.toAuthorDTO(author);
    }

    @Override
    @Transactional
    public AuthorDTO add(AuthorDTO authorDTO) {
        Author author = authorMapper.toAuthor(authorDTO);
        author = authorRepository.save(author);
        return authorMapper.toAuthorDTO(author);
    }

    @Override
    @Transactional
    public AuthorDTO getOrAdd(AuthorDTO authorDTO) {
        AuthorDTO result;
        try {
            result = get(authorDTO);
        }
        catch(NoSuchElementException e) {
            result = add(authorDTO);
        }
        return result;
    }

    @Override
    @Transactional
    public AuthorDTO update(AuthorDTO authorDTO) {
        Author source = authorMapper.toAuthor(authorDTO);
        Author target = authorRepository.findById(source.getId()).orElseThrow();
        target.setName(source.getName());
        target.setBooks(source.getBooks());
        target = authorRepository.save(target);
        return authorMapper.toAuthorDTO(target);
    }

    @Override
    public void addBook(AuthorDTO authorDTO, BookDTO bookDTO) {
        if(authorDTO.getId() == null || bookDTO.getId() == null) {
            throw new RuntimeException("Author id or book id doesnot specified!");
        }
        authorRepository.addBookToAuthor(authorMapper.toAuthor(authorDTO)
                , bookMapper.toBook(bookDTO));
    }

    @Override
    public void deleteBook(AuthorDTO authorDTO, BookDTO bookDTO) {
        if(authorDTO.getId() == null || bookDTO.getId() == null) {
            throw new RuntimeException("Author id or book id doesnot specified!");
        }
        authorRepository.deleteBookFromAuthor(authorMapper.toAuthor(authorDTO)
                , bookMapper.toBook(bookDTO));
    }

    @Override
    @Transactional
    public AuthorDTO delete(String id) {
        Author author = authorRepository.findById(id).orElseThrow();
        authorRepository.deleteById(id);
        //deleting author refs from books
        authorRepository.deleteAuthorFromBooks(author);
        return authorMapper.toAuthorDTO(author);
    }
}
