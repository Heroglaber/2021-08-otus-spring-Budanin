package ru.otus.library.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.models.converters.GenreMapper;
import ru.otus.library.models.domain.Genre;
import ru.otus.library.models.dto.GenreDTO;
import ru.otus.library.repositories.GenreRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GenreServiceImpl implements GenreService{
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public List<GenreDTO> getAll() {
        return genreMapper.toGenreDTOList(genreRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDTO get(String name) {
        Genre genre = genreRepository.findByName(name).orElseThrow();
        return genreMapper.toGenreDTO(genre);
    }

    @Override
    @Transactional
    public GenreDTO add(String name) {
        Genre genre = new Genre(name);
        genreRepository.save(genre);
        return genreMapper.toGenreDTO(genre);
    }

    @Override
    @Transactional
    public GenreDTO getOrAdd(String name) {
        GenreDTO genreDTO;
        try {
            genreDTO = get(name);
        }
        catch(NoSuchElementException e) {
            genreDTO = add(name);
        }
        return genreDTO;
    }

    @Override
    @Transactional
    public GenreDTO update(GenreDTO genreDTO) {
        Genre source = genreMapper.toGenre(genreDTO);
        Genre target = genreRepository.getById(source.getId());
        target.setName(source.getName());
        return genreMapper.toGenreDTO(target);
    }

    @Override
    @Transactional
    public GenreDTO delete(long id) {
        Genre genre = genreRepository.findById(id).orElseThrow();
        genreRepository.deleteById(id);
        return genreMapper.toGenreDTO(genre);
    }
}
