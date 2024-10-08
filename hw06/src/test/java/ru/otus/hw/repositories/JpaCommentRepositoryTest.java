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
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с комментариями")
@DataJpaTest
@Import({JpaCommentRepository.class})
class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository repository;

    @Autowired
    private TestEntityManager tem;

    private List<Comment> dbComment;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        List<Author> dbAuthors = getDbAuthors();
        List<Genre> dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        dbComment = getDbComments(dbBooks);
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void findByIdTest(Comment expectedComment) {
        var actualComment = repository.findById(expectedComment.getId());

        assertThat(actualComment).isPresent();
        recursiveComparingComment(actualComment.get(), expectedComment);
    }

    @DisplayName("должен загружать комментарии по книге")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findByBookIdTest(Book book) {
        var expectedComments = dbComment.stream()
                .filter(comment -> comment.getBook().getId() == book.getId())
                .toList();
        var actualComments = repository.findByBookId(book.getId());

        assertThat(expectedComments).containsExactlyElementsOf(actualComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void saveNewTest() {
        var expectedComment = new Comment(0, "Comment_456", dbBooks.get(0));

        var returnedComment = repository.save(expectedComment);

        var actualComment = tem.find(Comment.class, returnedComment.getId());
        recursiveComparingComment(actualComment, expectedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void saveUpdateTest() {
        var expectedComment = new Comment(1, "Comment_123", dbBooks.get(1));

        assertThat(repository.findById(expectedComment.getId()))
                .isPresent();

        var returnedComment = repository.save(expectedComment);

        var actualComment = tem.find(Comment.class, returnedComment.getId());
        assertThat(actualComment.getId())
                .isEqualTo(expectedComment.getId());
        recursiveComparingComment(actualComment, expectedComment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void deleteTest() {
        final long ID = 1L;
        var comment = repository.findById(ID);
        assertThat(comment).isPresent();
        repository.delete(comment.get());
        tem.flush();
        assertThat(tem.find(Comment.class, ID)).isNull();
    }

    private void recursiveComparingComment(Comment actualComment, Comment expectedComment) {
        assertThat(actualComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .comparingOnlyFields("content")
                .isEqualTo(expectedComment);

        var actualBook = actualComment.getBook();
        var expectedBook = expectedComment.getBook();
        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "title")
                .isEqualTo(expectedBook);

        assertThat(actualBook.getAuthor()).isNotNull()
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "fullName")
                .isEqualTo(expectedBook.getAuthor());

        assertThat(actualBook.getGenres()).isNotNull()
                .containsExactlyElementsOf(expectedBook.getGenres());
    }

    private static List<Comment> getDbComments() {
        var books = getDbBooks();
        return getDbComments(books);
    }

    private static List<Comment> getDbComments(List<Book> books) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Comment(id,
                        "Comment_" + id,
                        books.get(id - 1)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
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

}