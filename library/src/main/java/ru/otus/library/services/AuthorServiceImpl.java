package ru.otus.library.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.models.converters.AuthorMapper;
import ru.otus.library.models.domain.Author;
import ru.otus.library.models.dto.AuthorDTO;
import ru.otus.library.repositories.AuthorRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> getAll() {
        return authorMapper.toAuthorDTOList(authorRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDTO get(String name) {
        Author author = authorRepository.findByName(name).orElseThrow();
        return authorMapper.toAuthorDTO(author);
    }

    @Override
    @Transactional
    public AuthorDTO add(String name) {
        Author author = new Author(name);
        author = authorRepository.save(author);
        return authorMapper.toAuthorDTO(author);
    }

    @Override
    @Transactional
    public AuthorDTO getOrAdd(String name) {
        AuthorDTO authorDTO;
        try {
            authorDTO = get(name);
        }
        catch(NoSuchElementException e) {
            authorDTO = add(name);
        }
        return authorDTO;
    }

    @Override
    @Transactional
    public AuthorDTO update(AuthorDTO authorDTO) {
        Author source = authorMapper.toAuthor(authorDTO);
        Author target = authorRepository.getById(source.getId());
        target.setName(source.getName());
        target = authorRepository.save(target);
        return authorMapper.toAuthorDTO(target);
    }

    @Override
    @Transactional
    public AuthorDTO delete(long id) {
        Author author = authorRepository.findById(id).orElseThrow();
        authorRepository.deleteById(id);
        return authorMapper.toAuthorDTO(author);
    }
}
