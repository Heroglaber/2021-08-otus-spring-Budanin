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
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class BookDaoJdbc implements BookDao{
    private final NamedParameterJdbcOperations jdbc;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc, AuthorDao authorDao, GenreDao genreDao) {
        this.jdbc = jdbc;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public int count() {
        return jdbc.getJdbcOperations().queryForObject("select count(*) from books", Integer.class);
    }

    @Override
    public Book insert(Book book) {
        if(book.getGenre() != null) {
            Genre genre = book.getGenre();
            if (genre.getId() <= 0) {
                genre = genreDao.insert(genre);
            } else {
                genre = genreDao.getById(genre.getId());
            }
            KeyHolder kh = new GeneratedKeyHolder();
            MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                    "title", book.getTitle(),
                    "genreId", genre.getId()));
            jdbc.update("insert into books (title, genreId) values (:title, :genreId)",
                    params, kh);
            book.setId(kh.getKey().longValue());
        }
        else {
            KeyHolder kh = new GeneratedKeyHolder();
            MapSqlParameterSource params = new MapSqlParameterSource(Map.of("title", book.getTitle()));
            jdbc.update("insert into books (title) values (:title)",
                    params, kh);
            book.setId(kh.getKey().longValue());
        }
        if(book.getAuthors() != null && book.getAuthors().size() > 0) {
            for(Author author: book.getAuthors()) {
                if (author.getId() <= 0) {
                    author = authorDao.insert(author);
                } else {
                    author = authorDao.getById(author.getId());
                }
                MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                        "bookId", book.getId(),
                        "authorId", author.getId()));
                jdbc.update("insert into book_author (bookId, authorId) values (:bookId, :authorId)",
                        params);
            }
        }
        return book;
    }

    @Override
    public Book update(Book book) {
        if(book.getId() <= 0) {
            throw new InvalidKeyException("Book id is not specified.");
        }
        if(book.getGenre() != null) {
            Genre genre = book.getGenre();
            if (genre.getId() <= 0) {
                genre = genreDao.insert(genre);
            } else {
                genre = genreDao.update(genre);
            }
            MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                    "id", book.getId(),
                    "title", book.getTitle(),
                    "genreId", genre.getId()));
            jdbc.update("update books set title=:title, genreId=:genreId where id=:id", params);
        }
        else {
            MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                    "id", book.getId(),
                    "title", book.getTitle()));
            jdbc.update("update books set title=:title where id=:id", params);
        }
        if(book.getAuthors() != null && book.getAuthors().size() > 0) {
            for(Author author: book.getAuthors()) {
                if (author.getId() <= 0) {
                    author = authorDao.insert(author);
                } else {
                    author = authorDao.update(author);
                }
                if(!getAllRelations().contains(new BookAuthorRelation(book.getId(), author.getId()))) {
                    MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                            "bookId", book.getId(),
                            "authorId", author.getId()));
                    jdbc.update("insert into book_author (bookId, authorId) values (:bookId, :authorId)",
                            params);
                }
            }
        }
        return book;
    }

    @Override
    public Book addAuthor(Book book, Author authorInserted) {
        Author author = null;
        if (authorInserted.getId() <= 0) {
            author = authorDao.insert(new Author(authorInserted.getName()));
        } else {
            if(!authorInserted.getName().equals(authorDao.getById(authorInserted.getId()).getName())) {
                throw new InvalidKeyException("Author in database not equal to supplied.");
            }
            author = authorInserted;
        }

        if(book.getId() <= 0) {
            throw new InvalidKeyException("Book id is not specified.");
        }

        if(!getAllRelations().contains(new BookAuthorRelation(book.getId(), author.getId()))) {
            MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                    "bookId", book.getId(),
                    "authorId", author.getId()));
            jdbc.update("insert into book_author (bookId, authorId) values (:bookId, :authorId)",
                    params);
        }

        book.getAuthors().add(author);
        return book;
    }

    @Override
    public Book deleteAuthor(Book book, Author author) {
        if (author.getId() <= 0) {
            throw new InvalidKeyException("Author id is not specified.");
        }
        if (book.getId() <= 0) {
            throw new InvalidKeyException("Book id is not specified.");
        }
        if(getAllRelations().contains(new BookAuthorRelation(book.getId(), author.getId()))) {
            MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                    "bookId", book.getId(),
                    "authorId", author.getId()));
            jdbc.update("delete from book_author where bookId=:bookId and authorId=:authorId",
                    params);
        }

        book.getAuthors().remove(author);
        return book;
    }

    @Override
    public void delete(Book book) {
        if (book == null || book.getId() <= 0) {
            throw new InvalidKeyException("Book id is not specified.");
        }
        Map<String, Object> params = Collections.singletonMap("id", book.getId());
        //delete all relations with authors
        jdbc.update("delete from book_author where bookId=:id", params);
        //delete record from books table
        jdbc.update(
                "delete from books where id = :id", params
        );
    }

    @Override
    public Book findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Map<Long, Book> books =
                jdbc.query("select b.id, b.title, g.id as genreId, g.name as genreName, " +
                                "a.id as authorId, a.name as authorName " +
                                "from books as b left join genres as g on b.genreId = g.id " +
                                "left join book_author as ba on b.id = ba.bookId " +
                                "left join authors as a on ba.authorId = a.id " +
                                "where b.id = :id",
                        params, new BookResultSetExtractor());
        if(books.size() > 0) {
            return new ArrayList<>(Objects.requireNonNull(books).values()).get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public Book findByTitle(String title) {
        Map<String, Object> params = Collections.singletonMap("title", title);
        Map<Long, Book> books =
                jdbc.query("select b.id, b.title, g.id as genreId, g.name as genreName, " +
                                "a.id as authorId, a.name as authorName " +
                                "from books as b left join genres as g on b.genreId = g.id " +
                                "left join book_author as ba on b.id = ba.bookId " +
                                "left join authors as a on ba.authorId = a.id " +
                                "where b.title = :title",
                        params, new BookResultSetExtractor());
        if(books.size() > 0) {
            return new ArrayList<>(Objects.requireNonNull(books).values()).get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public List<Book> findAllWithAllInfo() {
        List<Author> authors = authorDao.findAllUsed();
        List<BookAuthorRelation> relations = getAllRelations();
        Map<Long, Book> books =
                    jdbc.query("select b.id, b.title, g.id as genreId, g.name as genreName " +
                                    "from books as b left join genres as g on b.genreId = g.id",
                        new BookResultSetExtractor());

        mergeStudentsInfo(books, authors, relations);
        return new ArrayList<>(Objects.requireNonNull(books).values());
    }

    private List<BookAuthorRelation> getAllRelations() {
        return jdbc.query("select bookId, authorId from book_author ba order by bookId, authorId",
                (rs, i) -> new BookAuthorRelation(rs.getLong("bookId"), rs.getLong("authorId")));
    }

    private void mergeStudentsInfo(Map<Long, Book> books, List<Author> authors,
                                   List<BookAuthorRelation> relations) {
        Map<Long, Author> authorsMap = authors.stream().collect(Collectors.toMap(Author::getId, Function.identity()));
        relations.forEach(r -> {
            if (books.containsKey(r.getBookId()) && authorsMap.containsKey(r.getAuthorId())) {
                books.get(r.getBookId()).getAuthors().add(authorsMap.get(r.getAuthorId()));
            }
        });
    }
}
