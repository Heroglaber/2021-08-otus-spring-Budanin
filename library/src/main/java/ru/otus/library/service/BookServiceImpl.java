package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.dao.BookDao;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{
    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book findById(long id) {
        return bookDao.findById(id);
    }

    @Override
    public Book findByTitle(String title) {
        return bookDao.findByTitle(title);
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAllWithAllInfo();
    }

    @Override
    public Book add(Book book) {
        return bookDao.insert(book);
    }

    @Override
    public Book update(Book book) {
        return bookDao.update(book);
    }

    @Override
    public Book addAuthor(Book book, Author author) {
        return bookDao.addAuthor(book, author);
    }

    @Override
    public Book deleteAuthor(Book book, Author author) {
        return bookDao.deleteAuthor(book, author);
    }

    public void delete(Book book) {
        bookDao.delete(book);
    }
}
