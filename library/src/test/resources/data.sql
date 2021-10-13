insert into genres (id, `name`) values (1, 'classic');
insert into genres (id, `name`) values (2, 'detective');
insert into genres (id, `name`) values (3, 'horror');
insert into genres (id, `name`) values (4, 'fantasy');

insert into authors (id, name) values (1, 'Arkady Strugatsky');
insert into authors (id, name) values (2, 'Boris Strugatsky');

insert into books (id, title, genreId) values (1, 'The Roadside Picnic', 4);
insert into books (id, title, genreId) values (2, 'The Final Circle of Paradise', 4);

insert into book_author (bookId, authorId) values (1, 1);
insert into book_author (bookId, authorId) values (1, 2);
insert into book_author (bookId, authorId) values (2, 1);
insert into book_author (bookId, authorId) values (2, 2);