package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Сервис для работы с комментариями")
@DataMongoTest
@Import({CommentServiceImpl.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl service;

    private List<Comment> dbComment;

    @BeforeEach
    void setUp() {
        dbComment = getDbComments();
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void findByIdTest(Comment expectedComment) {
        var actualComment = service.findById(expectedComment.getId());
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
    void findByBookIdTest(Book book) {
        var actualComments = service.findByBookId(book.getId());

        assertThat(actualComments).containsOnly(dbComment.get(Integer.parseInt(book.getId()) - 1));
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void insertTest() {
        List<Comment> comments = service.findByBookId("1");
        assertThat(comments).isNotEmpty();
        var expectedBook = comments.get(0).getBook();
        var expectedComment = Comment.builder().content("Comment_123").book(expectedBook).build();
        var returnedComment = service.insert(expectedComment.getContent(), "1");
        assertThat(returnedComment)
                .isNotNull()
                .matches(comment -> comment.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateTest() {
        var expectedComment = Comment.builder().id("1").book(Book.builder().id("1").build()).content("Comment_123").build();

        assertThat(service.findById(expectedComment.getId()))
                .isPresent();

        var returnedComment = service.update(expectedComment.getId(),
                expectedComment.getContent(), "1");

        assertThat(returnedComment)
                .isNotNull()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteTest() {
        var comment = service.findById("1");
        assertThat(comment).isPresent();
        service.deleteById(comment.get().getId());
        assertThat(service.findById("1")).isEmpty();
    }

    private static List<Comment> getDbComments() {
        return IntStream.range(1, 4).boxed()
                .map(id -> Comment.builder()
                        .id(Integer.toString(id))
                        .content("Comment_" + id).build())
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }


    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(Integer.toString(id),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(Integer.toString(id), "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(Integer.toString(id), "Genre_" + id))
                .toList();
    }

}