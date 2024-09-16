package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.mappers.BookMapperImpl;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с книгами")
@DataJpaTest
@Import({BookServiceImpl.class, BookMapperImpl.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl serviceTest;

    private List<AuthorDto> dbAuthors;

    private List<GenreDto> dbGenres;

    private List<BookDto> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findByIdTest(BookDto expectedBook) {
        var actualBook = serviceTest.findById(expectedBook.getId());

        assertThat(actualBook)
                .isNotNull()
                .isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void findAllTest() {
        var actualBooks = serviceTest.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void insertTest() {
        var expectedBook = new BookDto(0, "BookTitle_123", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));
        var returnedBook = serviceTest.insert(new BookDto(0, expectedBook.getTitle(),expectedBook.getAuthor(),
                expectedBook.getGenres()));

        assertThat(returnedBook)
                .isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateTest() {
        var expectedBook = new BookDto(1L, "BookTitle_123", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(serviceTest.findById(expectedBook.getId()))
                .isPresent();

        var returnedBook = serviceTest.update(new BookDto(expectedBook.getId(),
                expectedBook.getTitle(),
                expectedBook.getAuthor(),
                expectedBook.getGenres()));

        assertThat(returnedBook)
                .isNotNull()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteTest() {
        var book = serviceTest.findById(1L);
        assertThat(book).isPresent();
        serviceTest.deleteById(book.get().getId());
        assertThat(serviceTest.findById(1L)).isEmpty();
    }

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

}