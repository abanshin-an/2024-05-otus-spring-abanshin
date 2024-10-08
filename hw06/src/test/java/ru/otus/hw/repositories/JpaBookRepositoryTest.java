package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa  для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class,JpaGenreRepository.class})
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private TestEntityManager tem;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findByIdTest(Book expectedBook) {
        var actualBook = bookRepository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("id").isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void findAllTest() {
        var actualBooks = bookRepository.findAll();
        var expectedBooks = dbBooks;
        assertThat(actualBooks).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void saveNewBookTest() {
        var expectedBook = new Book(0, "BookTitle_123", dbAuthors.get(0),
                List.of(dbGenres.get(1), dbGenres.get(2)));
        var returnedBook = bookRepository.save(expectedBook);
        tem.flush();
        Book after = tem.find(Book.class, returnedBook.getId());
        assertThat(after).isNotNull();
        assertThat(after.getTitle()).isEqualTo("BookTitle_123");
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void saveUpdatedBookTest() {
        var expectedBook = new Book(1L, "BookTitle_456", dbAuthors.get(2),
                new ArrayList<>(List.of(dbGenres.get(4), dbGenres.get(5))));
        assertThat(expectedBook).isNotNull();
        var bookToUpdate = tem.find(Book.class, expectedBook.getId());
        assertThat(bookToUpdate.getTitle()).isEqualTo("BookTitle_1");
        bookToUpdate.setTitle(expectedBook.getTitle());
        bookToUpdate.setAuthor(expectedBook.getAuthor());

        bookToUpdate.setGenres(expectedBook.getGenres());

        var returnedBook = bookRepository.save(bookToUpdate);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        Book actualBook = tem.find(Book.class, returnedBook.getId());
        assertThat(actualBook).isNotNull();
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void deleteBookTest() {
        final long ID = 1L;
        var book = bookRepository.findById(ID);
        assertThat(book).isPresent();
        tem.flush();
        bookRepository.delete(book.get());
        assertThat(tem.find(Book.class, ID)).isNull();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}