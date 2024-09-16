package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id).map(bookMapper::modelToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookMapper.modelsToDto(bookRepository.findAll());
    }

    @Transactional
    @Override
    public BookDto insert(BookDto bookDto) {
        bookDto.setId(0);
        return save(bookDto);
    }

    @Transactional
    @Override
    public BookDto update(BookDto bookDto) {
        return save(bookDto);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.findById(id).ifPresent(bookRepository::delete);
    }

    private BookDto save(BookDto bookDto) {
        validate(bookDto);
        var book = bookMapper.modelFromDto(bookDto);
        return bookMapper.modelToDto(bookRepository.save(book));
    }

    private void validate(BookDto bookDto) {
        if (isEmpty(bookDto.getGenres())) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        if ( bookDto.getAuthor() == null ) {
            throw new IllegalArgumentException("Author id must not be null");
        }

        var authorId = bookDto.getAuthor().getId();

        authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));

        var genresIds=bookDto.getGenres().stream().map(GenreDto::getId).toList();

        var genres = genreRepository.findByIdIn(genresIds);

        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }
    }
}
