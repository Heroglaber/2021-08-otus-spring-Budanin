package ru.otus.library.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class BookDaoJdbc implements BookDao{
    private final NamedParameterJdbcOperations jdbc;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int count() {
        return jdbc.getJdbcOperations().queryForObject("select count(*) from books", Integer.class);
    }

    @Override
    public Book insert(Book book) {
        KeyHolder kh = new GeneratedKeyHolder();
        MapSqlParameterSource params =
                new MapSqlParameterSource(Map.of(
                        "title", book.getTitle(),
                        "genreId", book.getGenreId()));
        jdbc.update("insert into books (title, genreId) values (:title, :genreId)",
                params, kh);
        book.setId(kh.getKey().longValue());
        return book;
    }

    @Override
    public Book update(Book book) {
        KeyHolder kh = new GeneratedKeyHolder();
        MapSqlParameterSource params =
                new MapSqlParameterSource(Map.of(
                        "id", book.getId(),
                        "title", book.getTitle(),
                        "genreId", book.getGenreId()));
        jdbc.update("update books set title=:title, genreId=:genreId where id=:id",
                params, kh);
        book.setId(kh.getKey().longValue());

        return book;
    }

    @Override
    public Book getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return jdbc.queryForObject(
                    "select id, title, genreId from books where id = :id", params, new BookDaoJdbc.BookMapper()
            );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Book getByTitle(String title) {
        Map<String, Object> params = Collections.singletonMap("title", title);
        try {
            return jdbc.queryForObject(
                    "select top 1 id, title, genreId from books where title = :title", params, new BookDaoJdbc.BookMapper()
            );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Book> getAll() {
        try {
            return jdbc.query("select id, title, genreId from books", new BookDaoJdbc.BookMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Book> getAllByGenre(long genreId) {
        Map<String, Object> params = Collections.singletonMap("genreId", genreId);
        try {
            return jdbc.query("select id, title, genreId from books where genreId = :genreId", params, new BookDaoJdbc.BookMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
        }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update(
                "delete from books where id = :id", params
        );
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            long genreId = resultSet.getLong("genreId");
            return new Book(id, title, genreId);
        }
    }
}
