package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;

@Service
public class AuthorServiceImpl implements AuthorService{
    private AuthorDao authorDao;

    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public Author findByName(String authorName) {
        return authorDao.getByName(authorName);
    }

    @Override
    public Author findById(long id) {
        return authorDao.getById(id);
    }

    @Override
    public Author add(Author author) {
        return authorDao.insert(author);
    }
}
