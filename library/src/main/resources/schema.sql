drop table if exists authors;
create table authors (
    id bigserial,
    name varchar(800),
    primary key (id)
);
drop table if exists genres;
create table genres (
    id bigserial,
    name varchar(800),
    primary key (id)
);
drop table if exists books;
create table books (
    id bigserial,
    title varchar(800),
    primary key (id)
);
drop table if exists comments;
create table comments (
    id bigserial,
    book_id bigint references books(id) on delete cascade,
    message varchar(5000),
    primary key (id)
);
drop table if exists book_authors;
create table book_authors(
    book_id bigint references books(id) on delete cascade,
    author_id bigint references authors(id) on delete cascade,
    primary key (book_id, author_id)
);
drop table if exists book_genres;
create table book_genres(
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);