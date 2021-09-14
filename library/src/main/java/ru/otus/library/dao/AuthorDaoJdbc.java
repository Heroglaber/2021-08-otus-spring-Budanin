package ru.otus.library.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;

import javax.management.openmbean.InvalidKeyException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class AuthorDaoJdbc implements AuthorDao{

    private final NamedParameterJdbcOperations jdbc;

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int count() {
        return jdbc.getJdbcOperations().queryForObject("select count(*) from authors", Integer.class);
    }

    @Override
    public Author insert(Author author) {
        if (author.getId() > 0) {
            throw new InvalidKeyException("Author have ID. Maybe he is already in repository?");
        }
        KeyHolder kh = new GeneratedKeyHolder();
        MapSqlParameterSource params =
                new MapSqlParameterSource(Map.of( "name", author.getName()));
        jdbc.update("insert into authors (name) values (:name)", params, kh);
        author.setId(kh.getKey().longValue());
        return author;
    }

    @Override
    public int[] batchInsert(final List<Author> authors) {
        String sql = "insert into authors (name) values (:name)";
        List<Map<String, Object>> batchValues = new ArrayList<>(authors.size());
        for (Author author: authors) {
            if(author.getId() > 0) {
                throw new IllegalArgumentException("Author have ID. Maybe he is already in repository?");
            }
            batchValues.add(
                    new MapSqlParameterSource("name", author.getName())
                            .getValues());
        }
        int[] updateCounts = jdbc.batchUpdate(sql,
                batchValues.toArray(new Map[authors.size()]));
        return updateCounts;
    }

    @Override
    public Author update(Author author) {
        if(author.getId() <= 0) {
            throw new InvalidKeyException("Author id is not specified.");
        }
        MapSqlParameterSource params =
                new MapSqlParameterSource(Map.of(
                        "id", author.getId(),
                        "name", author.getName()
                ));
        jdbc.update("update authors set name=:name where id=:id", params);
        return author;
    }

    @Override
    public int[] batchUpdate(final List<Author> authors) {
        String sql = "update authors set name=:name where id=:id";
        List<Map<String, Object>> batchValues = new ArrayList<>(authors.size());
        for (Author author: authors) {
            if(author.getId() <= 0) {
                throw new InvalidKeyException("Author id is not specified.");
            }
            batchValues.add(
                    new MapSqlParameterSource("id", author.getId())
                    .addValue("name", author.getName())
                    .getValues());
        }
        int[] updateCounts = jdbc.batchUpdate(sql,
                batchValues.toArray(new Map[authors.size()]));
        return updateCounts;
    }

    @Override
    public Author getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return jdbc.queryForObject(
                    "select id, name from authors where id = :id", params, new AuthorRowMapper()
            );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Author getByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name);
        try {
            return jdbc.queryForObject(
                    "select id, name from authors where name = :name", params, new AuthorRowMapper()
            );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Author> getAll() {
        try {
            return jdbc.query("select id, name from authors", new AuthorRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Author> findAllUsed() {
        return jdbc.query("select a.id, a.name " +
                "from authors a inner join book_author ba on a.id = ba.authorid " +
                "group by a.id, a.name " +
                "order by a.name", new AuthorRowMapper());
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update(
                "delete from authors where id = :id", params
        );
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Author(id, name);
        }
    }
}
