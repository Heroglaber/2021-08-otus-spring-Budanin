package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.dao.BookAuthorDao;
import ru.otus.library.dao.BookDao;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookAuthor;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.BookDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService{
    private final BookDao bookDao;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;
    private final BookAuthorDao bookAuthorDao;


    public BookServiceImpl(BookDao bookDao, GenreDao genreDao, AuthorDao authorDao, BookAuthorDao bookAuthorDao) {
        this.bookDao = bookDao;
        this.genreDao = genreDao;
        this.authorDao = authorDao;
        this.bookAuthorDao = bookAuthorDao;
    }

    @Override
    public BookDTO addBook(BookDTO book) {
        Long genreId = addBookGenre(book.getGenre());
        List<Author> authors = addBookAuthors(book.getAuthors());
        Book insertedBook = bookDao.insert(new Book(book.getTitle(), genreId));
        //set id from inserted row
        book.setId(insertedBook.getId());
        //add records to the intermediate table
        for(Author author: authors){
            bookAuthorDao.insert(new BookAuthor(book.getId(), author.getId()));
        }
        return book;
    }

    @Override
    public BookDTO updateBook(BookDTO book) {
        Long genreId = addBookGenre(book.getGenre());
        List<Author> authors = addBookAuthors(book.getAuthors());
        Book insertedBook = bookDao.update(new Book(book.getId(), book.getTitle(), genreId));
        //set id from inserted row
        book.setId(insertedBook.getId());
        //add records to the intermediate table
        bookAuthorDao.deleteAllByBookId(book.getId());
        for(Author author: authors){
            bookAuthorDao.insert(new BookAuthor(book.getId(), author.getId()));
        }
        return book;
    }

    @Override
    public BookDTO addBookAuthors(long bookId, String... authorNames) {
        BookDTO book = mapBookDTO(bookDao.getById(bookId));
        if(book != null) {
            List<Author> authors = addBookAuthors(List.of(authorNames));
            for(Author author: authors) {
                bookAuthorDao.insert(new BookAuthor(book.getId(), author.getId()));
            }
        }
        return book;
    }

    private Long addBookGenre(String bookGenre) {
        if(bookGenre != null && !bookGenre.trim().isEmpty()) {
            Genre genre = genreDao.getByName(bookGenre);
            if (genre == null) {
                genre = genreDao.insert(bookGenre);
            }
            return genre.getId();
        }
        return null;
    }

    private List<Author> addBookAuthors(List<String> authorsNames) {
        List<Author> authors = new ArrayList<>();
        if(authorsNames != null) {
            for (String authorName : authorsNames) {
                Author currentAuthor = authorDao.getByName(authorName);
                if (currentAuthor == null) {
                    currentAuthor = authorDao.insert(authorName);
                }
                authors.add(currentAuthor);
            }
        }
        return authors;
    }

    @Override
    public BookDTO findByTitle(String title) {
        Book book = bookDao.getByTitle(title);
        return mapBookDTO(book);
    }

    @Override
    public BookDTO findById(long id) {
        Book book = bookDao.getById(id);
        return mapBookDTO(book);
    }

    @Override
    public List<BookDTO> getAll() {
        List<Book> books = bookDao.getAll();
        List<BookDTO> allBooks =  books.stream()
                .map(book -> mapBookDTO(book))
                .collect(Collectors.toList());
        return allBooks;
    }

    @Override
    public List<BookDTO> getAllByAuthor(String authorName) {
        Author author = authorDao.getByName(authorName);
        List<BookAuthor> bookAuthors = bookAuthorDao.getAllByAuthorId(author.getId());
        List<Book> books = bookAuthors.stream()
                .map(BookAuthor::getBookId)
                .map(bookId -> bookDao.getById(bookId))
                .collect(Collectors.toList());
        List<BookDTO> booksDTO =  books.stream()
                .map(book -> mapBookDTO(book))
                .collect(Collectors.toList());
        return booksDTO;
    }

    @Override
    public List<BookDTO> getAllByGenre(String genreName) {
        Genre genre = genreDao.getByName(genreName);
        if(genre != null) {
            List<Book> books = bookDao.getAllByGenre(genre.getId());
            List<BookDTO> booksDTO =  books.stream()
                    .map(book -> mapBookDTO(book))
                    .collect(Collectors.toList());
            return booksDTO;
        }
        else throw new NoSuchElementException("Genre not found by name.");
    }

    @Override
    public void deleteBookByTitle(String title) {
        BookDTO book = findByTitle(title);
        if(book != null) {
            bookDao.deleteById(book.getId());
        }
    }

    private BookDTO mapBookDTO(Book book) {
        if(book == null) {
            return null;
        }
        Genre genre = genreDao.getById(book.getGenreId());
        List<BookAuthor> bookAuthors = bookAuthorDao.getAllByBookId(book.getId());
        List<String> authors = new ArrayList<>();
        for(BookAuthor bookAuthor: bookAuthors) {
            Author author = authorDao.getById(bookAuthor.getAuthorId());
            authors.add(author.getName());
        }
        return new BookDTO(book.getId(), book.getTitle(), genre.getName(), authors);
    }
}
