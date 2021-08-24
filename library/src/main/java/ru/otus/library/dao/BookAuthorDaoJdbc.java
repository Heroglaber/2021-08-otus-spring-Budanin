package ru.otus.library.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.BookAuthor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class BookAuthorDaoJdbc implements BookAuthorDao{
    private final NamedParameterJdbcOperations jdbc;

    public BookAuthorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void insert(BookAuthor bookAuthor) {
        jdbc.update("insert into book_author (bookId, authorId) values (:bookId, :authorId)",
                Map.of("bookId", bookAuthor.getBookId(), "authorId", bookAuthor.getAuthorId()));
    }

    @Override
    public List<BookAuthor> getAllByBookId(long bookId) {
        try {
            return jdbc.query("select bookId, authorId from book_author where bookid = :bookId",
                    Map.of("bookId", bookId), new BookAuthorMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<BookAuthor> getAllByAuthorId(long authorId) {
        try {
            return jdbc.query("select bookId, authorId from book_author where authorId = :authorId",
                    Map.of("authorId", authorId), new BookAuthorMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteAllByBookId(long bookId) {
        Map<String, Object> params = Collections.singletonMap("bookId", bookId);
        jdbc.update(
                "delete from book_author where bookId = :bookId", params
        );
    }

    private static class BookAuthorMapper implements RowMapper<BookAuthor> {

        @Override
        public BookAuthor mapRow(ResultSet resultSet, int i) throws SQLException {
            long bookId = resultSet.getLong("bookId");
            long authorId = resultSet.getLong("authorId");
            return new BookAuthor(bookId, authorId);
        }
    }
}
