drop table if exists authors;
drop table if exists genres;
drop table if exists books;
drop table if exists books_genres;
drop table if exists comments;

create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table comments
(
    id bigserial,
    content varchar,
    book_id bigint references books (id) on delete cascade,
    primary key (id)
)