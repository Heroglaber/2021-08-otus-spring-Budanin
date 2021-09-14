package ru.otus.library.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.ex.BookAuthorRelation;
import ru.otus.library.ex.BookResultSetExtractor;

import javax.management.openmbean.InvalidKeyException;
import java.util.*;

@Repository
public class BookDaoJdbc implements BookDao{
    private final NamedParameterJdbcOperations jdbc;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookAuthorRelationDao relationDao;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc, AuthorDao authorDao, GenreDao genreDao, BookAuthorRelationDao relationDao) {
        this.jdbc = jdbc;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.relationDao = relationDao;
    }

    @Override
    public int count() {
        return jdbc.getJdbcOperations().queryForObject("select count(*) from books", Integer.class);
    }

    @Override
    public Book insert(Book book) {
        String sql = null;
        MapSqlParameterSource params = null;
        Genre genre = updateGenre(book.getGenre());
        if(genre != null) {
            sql = "insert into books (title, genreId) values (:title, :genreId)";
            params = new MapSqlParameterSource(Map.of("title", book.getTitle(),
                    "genreId", genre.getId()));
        }
        else {
            sql = "insert into books (title) values (:title)";
            params = new MapSqlParameterSource(Map.of("title", book.getTitle()));
        }
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(sql, params, kh);
        book.setId(kh.getKey().longValue());
        updateAuthors(book.getId(), book.getAuthors());
        return book;
    }

    @Override
    public Book update(Book book) {
        if(book.getId() <= 0) { throw new InvalidKeyException("Book id is not specified."); }
        String sql = null;
        MapSqlParameterSource params = null;
        Genre genre = updateGenre(book.getGenre());
        if(genre != null) {
            params = new MapSqlParameterSource(Map.of("id", book.getId(),
                    "title", book.getTitle(),
                    "genreId", genre.getId()));
            sql = "update books set title=:title, genreId=:genreId where id=:id";
        }
        else {
            params = new MapSqlParameterSource(Map.of("id", book.getId(),
                    "title", book.getTitle()));
            sql = "update books set title=:title where id=:id";
        }
        jdbc.update(sql, params);

        updateAuthors(book.getId(), book.getAuthors());
        return book;
    }

    private Genre updateGenre(Genre genre) {
        if(genre != null) {
            if (genre.getId() <= 0) {
                genre = genreDao.insert(genre);
            } else {
                genre = genreDao.update(genre);
            }
        }
        return genre;
    }

    private void updateAuthors(long bookId, List<Author> authors) {
        //remove old authors
        relationDao.deleteAllByBookId(bookId);
        List<BookAuthorRelation> relations = new ArrayList<>();
        if(!authors.isEmpty()) {
            for(Author author: authors) {
                if (author.getId() <= 0) {
                    author = authorDao.insert(author);
                } else {
                    author = authorDao.update(author);
                }
                if(relationDao.get(bookId, author.getId()) == null) {
                    relations.add(new BookAuthorRelation(bookId, author.getId()));
                }
            }
            relationDao.batchInsert(relations);
        }
    }

    @Override
    public Book addAuthor(Book book, Author authorInserted) {
        if(book.getId() <= 0) {
            throw new InvalidKeyException("Book id is not specified.");
        }
        Author author = authorDao.insert(authorInserted);
        if(relationDao.get(book.getId(), author.getId()) == null) {
            relationDao.insert(new BookAuthorRelation(book.getId(), author.getId()));
        }
        book.getAuthors().add(author);
        return book;
    }

    @Override
    public Book deleteAuthor(Book book, Author author) {
        if (book.getId() <= 0) {
            throw new InvalidKeyException("Book id is not specified.");
        }
        if(relationDao.get(book.getId(), author.getId()) != null) {
            relationDao.delete(new BookAuthorRelation(book.getId(), author.getId()));
        }
        book.getAuthors().remove(author);
        return book;
    }

    @Override
    public void delete(Book book) {
        if (book == null || book.getId() <= 0) {
            throw new InvalidKeyException("Book id is not specified.");
        }
        jdbc.update("delete from books where id = :id",
                new MapSqlParameterSource("id", book.getId()));
    }

    @Override
    public Book findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        List<Book> books =
                jdbc.query("select b.id, b.title, g.id as genreId, g.name as genreName, " +
                                "a.id as authorId, a.name as authorName " +
                                "from books as b left join genres as g on b.genreId = g.id " +
                                "left join book_author as ba on b.id = ba.bookId " +
                                "left join authors as a on ba.authorId = a.id " +
                                "where b.id = :id",
                        params, new BookResultSetExtractor());
        if(books.size() > 0) {
            return books.get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public Book findByTitle(String title) {
        Map<String, Object> params = Collections.singletonMap("title", title);
        List<Book> books =
                jdbc.query("select b.id, b.title, g.id as genreId, g.name as genreName, " +
                                "a.id as authorId, a.name as authorName " +
                                "from books as b left join genres as g on b.genreId = g.id " +
                                "left join book_author as ba on b.id = ba.bookId " +
                                "left join authors as a on ba.authorId = a.id " +
                                "where b.title = :title",
                        params, new BookResultSetExtractor());
        if(books.size() > 0) {
            return books.get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public List<Book> findAllWithAllInfo() {
        List<Book> books =
                    jdbc.query("select b.id, b.title, g.id as genreId, g.name as genreName, " +
                                    "a.id as authorId, a.name as authorName " +
                                    "from books as b left join genres as g on b.genreId = g.id " +
                                    "left join book_author as ba on b.id = ba.bookId " +
                                    "left join authors as a on ba.authorId = a.id ",
                        new BookResultSetExtractor());
        return books;
    }
}
