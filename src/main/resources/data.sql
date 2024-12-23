insert into authors(id, first_name, last_name, description)
values (10, 'John', 'Doe', 'Description'),
(11, 'Anton', 'Surname', 'Desciption');

insert into users(id, first_name, last_name, email, password, birthday)
values (10, 'Jane', 'Doe', 'jane_doe@email.com', 'Password', '1998-11-12');

insert into books(id, title, description, genre, pages_count, author_id)
values (10, 'Title', 'Description', 'FICTION', 123, 10),
(11, 'Book', 'Book description', 'FICTION', 94, 10);

insert into borrowed_books(book_id, user_id, borrow_date, return_date)
values (10, 10, '2024-12-23 13:21:54.819000 +00:00', '2024-12-23 13:21:54.819000 +00:00')