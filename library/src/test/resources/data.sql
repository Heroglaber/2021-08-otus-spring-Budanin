insert into genres (id, name) values (1, 'classic');
insert into genres (id, name) values (2, 'detective');
insert into genres (id, name) values (3, 'horror');
insert into genres (id, name) values (4, 'fantasy');
insert into genres (id, name) values (5, 'novel');
insert into genres (id, name) values (6, 'unknown');

insert into authors (id, name) values (1, 'Arkady Strugatsky');
insert into authors (id, name) values (2, 'Boris Strugatsky');
insert into authors (id, name) values (3, 'Gabriel Garcia Marquez');
insert into authors (id, name) values (4, 'Louisa May Alcott');
insert into authors (id, name) values (5, 'Toni Morrison');
insert into authors (id, name) values (6, 'Delia Owens');
insert into authors (id, name) values (7, 'Jeannette Walls');
insert into authors (id, name) values (8, 'Stephen King');
insert into authors (id, name) values (9, 'Trevor Noah');
insert into authors (id, name) values (10, 'Josh Malerman');


insert into books (id, title) values (1, 'The Roadside Picnic');
insert into books (id, title) values (2, 'The Final Circle of Paradise');
insert into books (id, title) values (3, 'One Hundred Years of Solitude');
insert into books (id, title) values (4, 'Little Women');
insert into books (id, title) values (5, 'Beloved');
insert into books (id, title) values (6, 'Where the Crawdads Sing');
insert into books (id, title) values (7, 'The Glass Castle');
insert into books (id, title) values (8, 'Carrie');
insert into books (id, title) values (9, 'Born a Crime');
insert into books (id, title) values (10, 'Bird Box');

insert into comments(book_id, message) values (1, 'Greatest book ever!');
insert into comments(book_id, message) values (1, 'I read this book as a child.');
insert into comments(book_id, message) values (8, 'Scary book!');
insert into comments(book_id, message) values (8, 'JUST AMAZING');

insert into book_authors (book_id, author_id) values (1, 1);
insert into book_authors (book_id, author_id) values (1, 2);
insert into book_authors (book_id, author_id) values (2, 1);
insert into book_authors (book_id, author_id) values (2, 2);
insert into book_authors (book_id, author_id) values (3, 3);
insert into book_authors (book_id, author_id) values (4, 4);
insert into book_authors (book_id, author_id) values (5, 5);
insert into book_authors (book_id, author_id) values (6, 6);
insert into book_authors (book_id, author_id) values (7, 7);
insert into book_authors (book_id, author_id) values (8, 8);
insert into book_authors (book_id, author_id) values (9, 9);
insert into book_authors (book_id, author_id) values (10, 10);

insert into book_genres (book_id, genre_id) values (1, 4);
insert into book_genres (book_id, genre_id) values (2, 4);
insert into book_genres (book_id, genre_id) values (3, 5);
insert into book_genres (book_id, genre_id) values (4, 5);
insert into book_genres (book_id, genre_id) values (5, 5);
insert into book_genres (book_id, genre_id) values (6, 5);
insert into book_genres (book_id, genre_id) values (7, 6);
insert into book_genres (book_id, genre_id) values (8, 3);
insert into book_genres (book_id, genre_id) values (8, 5);
insert into book_genres (book_id, genre_id) values (9, 6);
insert into book_genres (book_id, genre_id) values (10, 5);