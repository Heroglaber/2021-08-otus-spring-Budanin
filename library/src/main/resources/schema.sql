DROP TABLE IF EXISTS AUTHORS;
CREATE TABLE AUTHORS(
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
);
DROP TABLE IF EXISTS GENRES;
CREATE TABLE GENRES(
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL
);
DROP TABLE IF EXISTS BOOKS;
CREATE TABLE BOOKS(
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      genreId BIGINT,
                      foreign key (genreId) references GENRES(id) ON DELETE SET NULL
);
DROP TABLE IF EXISTS BOOK_AUTHOR;
CREATE TABLE BOOK_AUTHOR(
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      bookId BIGINT NOT NULL,
                      authorId BIGINT NOT NULL,
                      foreign key (bookId) references BOOKS(id) ON DELETE CASCADE,
                      foreign key (authorId) references AUTHORS(id) ON DELETE CASCADE
);