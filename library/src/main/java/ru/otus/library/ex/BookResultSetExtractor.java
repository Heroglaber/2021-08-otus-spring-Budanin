package ru.otus.library.ex;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class BookResultSetExtractor  implements
        ResultSetExtractor<List<Book>> {
    @Override
    public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Book> books = new HashMap<>();
        while (rs.next()) {
            long id = rs.getLong("id");
            Book book = books.get(id);
            if (book == null) {
                book = new Book(id, rs.getString("title"),
                        new Genre(rs.getLong("genreId"), rs.getString("genreName")),
                        new ArrayList<>());
                books.put(book.getId(), book);
            }
            if(hasColumn(rs, "authorId")) {
                book.getAuthors().add(new Author(rs.getLong("authorId")
                        , rs.getString("authorName")));
            }
        }
        return new ArrayList<>(Objects.requireNonNull(books).values());
    }

    public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equalsIgnoreCase(rsmd.getColumnLabel(x))) {
                return true;
            }
        }
        return false;
    }
}
