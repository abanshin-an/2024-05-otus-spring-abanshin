package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.GenreMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("Сервис для работы с книгами должен")
@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({BookServiceImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
@Sql(value = "/scriptBeforeTest.sql")
class BookServiceImplTest {

    private static final long FIRST_BOOK_ID = 1L;
    private static final int BOOKS_COUNT = 3;
    private static final String NEW_TITLE_FOR_NEW_BOOK = "new title";


    @Autowired
    private BookService bookService;

    private BookDto bookById1;

    @BeforeEach
    void setUp() {
        bookById1 = new BookDto(FIRST_BOOK_ID, "BookTitle_1",
                new AuthorDto(1L, "Author_1"),
                new GenreDto(1L, "Genre_1"));
    }

    @DisplayName("вернуть книгу по ее id")
    @Test
    void findById() {
        var book = bookService.findById(FIRST_BOOK_ID);
        assertThat(book).isEqualTo(bookById1);
    }

    @DisplayName("вернуть все книги")
    @Test
    void findAll() {
        var books = bookService.findAll();
        assertThat(books).isNotEmpty().size().isEqualTo(BOOKS_COUNT);
        assertThat(books.get(0)).isNotNull().isEqualTo(bookById1);
        assertThat(books.get(0).getAuthorDto()).isNotNull().isEqualTo(bookById1.getAuthorDto());
        assertThat(books.get(0).getGenreDto()).isNotNull().isEqualTo(bookById1.getGenreDto());
    }

    @DisplayName("сохранить новую книгу")
    @Test
    void insert() {
        var expectedBook = new BookDto(0, NEW_TITLE_FOR_NEW_BOOK, bookById1.getAuthorDto(), bookById1.getGenreDto());

        var savedBook = bookService.insert(expectedBook);
        assertThat(savedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .hasFieldOrPropertyWithValue("title", expectedBook.getTitle())
                .hasFieldOrPropertyWithValue("authorDto", expectedBook.getAuthorDto())
                .hasFieldOrPropertyWithValue("genreDto", expectedBook.getGenreDto());

        assertThat(savedBook.getAuthorDto().getFullName()).isEqualTo(expectedBook.getAuthorDto().getFullName());
        assertThat(savedBook.getGenreDto().getName()).isEqualTo(expectedBook.getGenreDto().getName());

        assertThat(bookService.findById(savedBook.getId()))
                .isEqualTo(savedBook);

        assertThat(bookService.findAll().size()).isEqualTo(BOOKS_COUNT + 1);
    }

    @DisplayName("сохранить обновленную книгу")
    @Test
    void update() {
        var expectedBook = new BookDto(FIRST_BOOK_ID, NEW_TITLE_FOR_NEW_BOOK, bookById1.getAuthorDto(),
                bookById1.getGenreDto());

        var updatedBook = bookService.update(FIRST_BOOK_ID, expectedBook);

        assertThat(updatedBook).isNotNull().isEqualTo(expectedBook);

        assertThat(updatedBook.getAuthorDto().getFullName()).isEqualTo(bookById1.getAuthorDto().getFullName());
        assertThat(updatedBook.getGenreDto().getName()).isEqualTo(bookById1.getGenreDto().getName());

        assertThat(bookService.findById(updatedBook.getId()))
                .isEqualTo(updatedBook);

        assertThat(bookService.findAll().size()).isEqualTo(BOOKS_COUNT);
    }

    @DisplayName("удалить книгу по id")
    @Test
    void deleteById() {
        assertThat(bookService.findById(FIRST_BOOK_ID)).isNotNull();
        bookService.deleteById(FIRST_BOOK_ID);
        assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(FIRST_BOOK_ID));
    }
}
