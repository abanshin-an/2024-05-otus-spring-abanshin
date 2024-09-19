package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.BookDto;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookDtoConverter {

    private final AuthorDtoConverter authorDtoConverter;

    private final GenreDtoConverter genreDtoConverter;

    public String bookToString(BookDto book) {
        var genresString = book.getGenres().stream()
                .map(genreDtoConverter::convert)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorDtoConverter.convert(book.getAuthor()),
                genresString);
    }
}
