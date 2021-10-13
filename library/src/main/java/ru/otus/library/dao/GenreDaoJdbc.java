package ru.otus.library.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Genre;

import javax.management.openmbean.InvalidKeyException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class GenreDaoJdbc implements GenreDao{

    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int count() {
        return jdbc.getJdbcOperations().queryForObject("select count(*) from genres", Integer.class);
    }

    @Override
    public Genre insert(Genre genre) {
        KeyHolder kh = new GeneratedKeyHolder();
        MapSqlParameterSource params =
                new MapSqlParameterSource(Map.of( "name", genre.getName()));
        jdbc.update("insert into genres (name) values (:name)", params, kh);
        genre.setId(kh.getKey().longValue());
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        if(genre.getId() <= 0) {
            throw new InvalidKeyException("Author id is not specified.");
        }
        MapSqlParameterSource params =
                new MapSqlParameterSource(Map.of(
                        "id", genre.getId(),
                        "name", genre.getName()
                ));
        jdbc.update("update genres set name=:name where id=:id", params);
        return genre;
    }

    @Override
    public Genre getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return jdbc.queryForObject(
                    "select id, name from genres where id = :id", params, new GenreRowMapper()
            );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Genre getByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name);
        try {
            return jdbc.queryForObject(
                    "select top 1 id, name from genres where name = :name", params, new GenreRowMapper()
            );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        try {
            return jdbc.query("select id, name from genres", new GenreRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update(
                "delete from genres where id = :id", params
        );
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Genre(id, name);
        }
    }
}
