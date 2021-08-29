package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.dao.BookAuthorDao;
import ru.otus.library.dao.BookDao;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookAuthor;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookAuthorServiceImpl implements BookAuthorService{
    private final BookAuthorDao bookAuthorDao;
    private final BookDao bookDao;
    private final AuthorDao authorDao;

    public BookAuthorServiceImpl(BookAuthorDao bookAuthorDao, BookDao bookDao, AuthorDao authorDao) {
        this.bookAuthorDao = bookAuthorDao;
        this.bookDao = bookDao;
        this.authorDao = authorDao;
    }

    @Override
    public void addBookAuthor(long bookId, long authorId) {
        bookAuthorDao.insert(new BookAuthor(bookId, authorId));
    }

    @Override
    public List<Author> getAllAuthorsOfBook(long bookId) {
        List<BookAuthor> bookAuthors = bookAuthorDao.getAllByBookId(bookId);
        List<Author> authors = new ArrayList<>();
        for(BookAuthor bookAuthor: bookAuthors){
            authors.add(authorDao.getById(bookAuthor.getAuthorId()));
        }
        return authors;
    }

    @Override
    public List<Book> getAllBooksOfAuthor(long authorId) {
        List<BookAuthor> bookAuthors = bookAuthorDao.getAllByAuthorId(authorId);
        List<Book> books = new ArrayList<>();
        for(BookAuthor bookAuthor: bookAuthors){
            books.add(bookDao.getById(bookAuthor.getBookId()));
        }
        return books;
    }
}
