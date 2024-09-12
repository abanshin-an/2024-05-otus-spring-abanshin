create table authors
(
    id        integer auto_increment,
    full_name varchar(255),
    primary key (id)
);

create table genres
(
    id   integer auto_increment,
    name varchar(255),
    primary key (id)
);

create table books
(
    id        integer auto_increment,
    title     varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table books_genres
(
    book_id  bigint references books (id) on delete cascade,
    genre_id bigint references genres (id) on delete cascade,
    primary key (book_id, genre_id)
);

create table comments
(
    id      integer auto_increment,
    text    varchar,
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);