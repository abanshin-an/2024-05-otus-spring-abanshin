drop table COMMENTS;
drop table BOOKS;
drop table AUTHORS;
drop table GENRES;

create table authors
(
    id        bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres
(
    id   bigserial,
    name varchar(255),
    primary key (id)
);

create table books
(
    id        bigserial,
    title     varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id  bigint references genres (id) on delete cascade,
    primary key (id)
);

create table comments
(
    id      bigserial,
    text varchar,
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);

insert into authors(full_name)
values ('Author_1'),
       ('Author_2'),
       ('Author_3');

insert into genres(name)
values ('Genre_1'),
       ('Genre_2'),
       ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1),
       ('BookTitle_2', 2, 2),
       ('BookTitle_3', 3, 3);

insert into comments(text, BOOK_ID)
values ('Comment_1_1', 1),
       ('Comment_1_2', 1),
       ('Comment_2_1', 2),
       ('Comment_2_2', 2),
       ('Comment_3_1', 3),
       ('Comment_3_2', 3);
