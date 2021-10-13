package ru.otus.library.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.library.ex.BookAuthorRelation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class BookAuthorRelationDaoJdbc implements BookAuthorRelationDao{
    private final NamedParameterJdbcOperations jdbc;

    public BookAuthorRelationDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<BookAuthorRelation> getAll() {
        try {
            return jdbc.query("select bookId, authorId from book_author ba order by bookId, authorId",
                    (rs, i) -> new BookAuthorRelation(rs.getLong("bookId"), rs.getLong("authorId")));
        }
        catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<BookAuthorRelation> getByBookId(long bookId) {
        MapSqlParameterSource params = new MapSqlParameterSource("bookId", bookId);
        try {
            return jdbc.query("select bookId, authorId from book_author ba where bookId = :bookId order by bookId, authorId",
                    params,
                    (rs, i) -> new BookAuthorRelation(rs.getLong("bookId"), rs.getLong("authorId")));
        }
        catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<BookAuthorRelation> getByAuthorId(long authorId) {
        MapSqlParameterSource params = new MapSqlParameterSource("authorId", authorId);
        try {
            return jdbc.query("select bookId, authorId from book_author ba where authorId = :authorId order by bookId, authorId",
                    params,
                    (rs, i) -> new BookAuthorRelation(rs.getLong("bookId"), rs.getLong("authorId")));
        }
            catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public BookAuthorRelation get(long bookId, long authorId) {
        MapSqlParameterSource params = new MapSqlParameterSource("bookId", bookId)
                .addValue("authorId", authorId);
        try {
            return jdbc.queryForObject("select bookId, authorId from book_author ba where bookId = :bookId and authorId = :authorId order by bookId, authorId",
                    params,
                    (rs, i) -> new BookAuthorRelation(rs.getLong("bookId"), rs.getLong("authorId")));
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int[] batchInsert(List<BookAuthorRelation> relations) {
        String sql = "insert into book_author (bookId, authorId) values (:bookId, :authorId)";
        List<Map<String, Object>> batchValues = new ArrayList<>(relations.size());
        for (BookAuthorRelation relation: relations) {
            batchValues.add(
                    new MapSqlParameterSource("bookId", relation.getBookId())
                            .addValue("authorId", relation.getAuthorId())
                            .getValues());
        }
        int[] updateCounts = jdbc.batchUpdate(sql,
                batchValues.toArray(new Map[relations.size()]));
        return updateCounts;
    }

    @Override
    public BookAuthorRelation insert(BookAuthorRelation bookAuthorRelation) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "bookId", bookAuthorRelation.getBookId(),
                "authorId", bookAuthorRelation.getAuthorId()));
        jdbc.update("insert into book_author (bookId, authorId) values (:bookId, :authorId)",
                params);
        return bookAuthorRelation;
    }

    @Override
    public void delete(BookAuthorRelation bookAuthorRelation) {
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "bookId", bookAuthorRelation.getBookId(),
                "authorId", bookAuthorRelation.getAuthorId()));
        jdbc.update("delete from book_author where bookId=:bookId and authorId=:authorId",
                params);
    }

    @Override
    public void deleteAllByBookId(long bookId) {
        Map<String, Object> params = Collections.singletonMap("id", bookId);
        jdbc.update("delete from book_author where bookId=:id", params);
    }
}
