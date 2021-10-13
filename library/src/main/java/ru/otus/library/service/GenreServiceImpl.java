package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Genre;

@Service
public class GenreServiceImpl implements GenreService{
    private final GenreDao genreDao;

    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Genre findByName(String genreName) {
        return genreDao.getByName(genreName);
    }

    @Override
    public Genre add(Genre genre) {
        return genreDao.insert(genre);
    }
}
