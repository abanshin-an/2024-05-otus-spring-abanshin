package ru.otus.hw.services;

import ru.otus.hw.dtos.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto insert(BookDto bookDto);

    BookDto update(BookDto bookDto);

    void deleteById(long id);
}
