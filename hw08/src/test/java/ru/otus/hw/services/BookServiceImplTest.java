package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с книгами")
@DataMongoTest
@Import({BookServiceImpl.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl serviceTest;

    @Autowired
    private MongoTemplate mongoTemplate;

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
        var expectedBook = Book.builder().title( "BookTitle_10500").author(dbAuthors.get(0)).
                genres(List.of(dbGenres.get(0), dbGenres.get(2))).build();
        var returnedBook = serviceTest.insert(expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));

        assertThat(returnedBook)
                .isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateTest() {
        var expectedBook = new Book("1", "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(serviceTest.findById(expectedBook.getId()))
                .isPresent();

        var returnedBook = serviceTest.update(expectedBook.getId(),
                expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));

        assertThat(returnedBook)
                .isNotNull()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void deleteTest() {
        BasicQuery commentQuery = new BasicQuery("{'book.$id' : '1' }");
        var book = mongoTemplate.findById("1", Book.class);
        var comment = mongoTemplate.find(commentQuery, Comment.class);
        assertThat(book).isNotNull();
        assertThat(comment).hasSize(1);

        serviceTest.deleteById(book.getId());

        assertThat(mongoTemplate.findById("1",Book.class)).isNull();
        assertThat(mongoTemplate.find(commentQuery, Comment.class)).hasSize(0);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(String.format("%s", id), "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(String.format("%s", id), "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(String.format("%s", id),
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