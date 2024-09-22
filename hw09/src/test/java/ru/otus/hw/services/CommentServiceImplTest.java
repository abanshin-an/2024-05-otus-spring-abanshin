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
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.CommentMapperImpl;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с комментариями")
@DataJpaTest
@Import({CommentServiceImpl.class, CommentMapperImpl.class, BookMapperImpl.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl serviceTest;

    private List<CommentDto> dbComment;

    @BeforeEach
    void setUp() {
        dbComment = getDbComments();
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void findByIdTest(CommentDto expectedComment) {
        var actualComment = serviceTest.findById(expectedComment.getId());
        assertThat(actualComment).isPresent();
        assertThat(actualComment)
                .isNotNull()
                .isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарии по книге")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findByBookIdTest(BookDto book) {
        var expectedComments = dbComment.get((int) book.getId() - 1);
        var actualComments = serviceTest.findByBookId(book.getId());

        assertThat(actualComments).containsOnly(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void insertTest() {
        var expectedComment = new Comment(0, "Comment_123", new Book());
        var returnedComment = serviceTest.insert(expectedComment.getContent(), 1);
        assertThat(returnedComment)
                .isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison()
                .ignoringFields("id","book", "author")
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void saveTest() {
        var expectedComment = new CommentDto(1, "Comment_123", getDbBooks().get(0));

        assertThat(serviceTest.findById(expectedComment.getId()))
                .isPresent();

        var returnedComment = serviceTest.update(expectedComment.getId(),
                expectedComment.getContent(), 1);

        assertThat(returnedComment)
                .isNotNull()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteTest() {
        var comment = serviceTest.findById(1L);
        assertThat(comment).isPresent();
        serviceTest.deleteById(comment.get().getId());
        assertThat(serviceTest.findById(1L)).isEmpty();
    }

    private static List<CommentDto> getDbComments() {
        var dbBooks = getDbBooks();
        return IntStream.range(1, 4).boxed()
                .map(id -> new CommentDto(id, "Comment_" + id, dbBooks.get(id-1)))
                .toList();
    }

    private static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
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

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static  List<GenreDto> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

}