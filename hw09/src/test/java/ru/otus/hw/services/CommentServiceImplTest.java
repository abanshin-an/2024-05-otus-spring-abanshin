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
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.CommentMapperImpl;
import ru.otus.hw.mappers.GenreMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Сервис для работы с комментариями должен")
@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({CommentServiceImpl.class, CommentMapperImpl.class,
        BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
@Sql(value = "/scriptBeforeTest.sql")
class CommentServiceImplTest {

    private static final int COMMENTS_COUNT_BY_FIRST_ID_BOOK = 2;
    private static final long FIRST_COMMENT_ID = 1L;
    private static final long FIRST_BOOK_ID = 1L;
    private static final String FIRST_COMMENT_TEXT = "Comment_1_1";
    private static final String NEW_COMMENT_TEXT = "NEW COMMENT";

    @Autowired
    private CommentService commentService;

    private BookDto bookById1;

    private CommentDto commentForBookWithId1;

    @BeforeEach
    void setUp() {
        bookById1 = new BookDto(FIRST_BOOK_ID, "BookTitle_1",
                new AuthorDto(1L, "Author_1"),
                new GenreDto(1L, "Genre_1"));
        commentForBookWithId1 = new CommentDto(FIRST_COMMENT_ID, FIRST_COMMENT_TEXT, bookById1);
    }

    @DisplayName("вернуть комментарий по его id")
    @Test
    void findById() {
        var comment = commentService.findById(FIRST_COMMENT_ID);

        assertThat(comment).isEqualTo(commentForBookWithId1);
        assertThat(comment.getBookDto()).isEqualTo(bookById1);
        assertThat(comment.getBookDto().getAuthorDto()).isEqualTo(bookById1.getAuthorDto());
        assertThat(comment.getBookDto().getGenreDto()).isEqualTo(bookById1.getGenreDto());
    }

    @DisplayName("вернуть все комментарии для книги по ее id")
    @Test
    void findAllByBookId() {
        var comments = commentService.findAllByBookId(FIRST_BOOK_ID);

        assertThat(comments.size()).isEqualTo(COMMENTS_COUNT_BY_FIRST_ID_BOOK);
        assertThat(comments.get(0)).isEqualTo(commentForBookWithId1);
        assertThat(comments.get(0).getBookDto()).isEqualTo(bookById1);
    }

    @DisplayName("сохранить новый комментарий")
    @Test
    void insert() {
        var expectedComment = new CommentDto(0, NEW_COMMENT_TEXT, bookById1);

        var savedComment = commentService.insert(NEW_COMMENT_TEXT, bookById1.getId());
        assertThat(savedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .hasFieldOrPropertyWithValue("text", expectedComment.getText())
                .hasFieldOrPropertyWithValue("bookDto", expectedComment.getBookDto());

        assertThat(savedComment.getBookDto().getGenreDto()).isEqualTo(bookById1.getGenreDto());
        assertThat(savedComment.getBookDto().getAuthorDto()).isEqualTo(bookById1.getAuthorDto());

        assertThat(commentService.findById(savedComment.getId()))
                .isEqualTo(savedComment);

        assertThat(commentService.findAllByBookId(bookById1.getId()))
                .size().isEqualTo(COMMENTS_COUNT_BY_FIRST_ID_BOOK + 1);
    }

    @DisplayName("сохранять измененный комментарий")
    @Test
    void update() {
        var expectedComment = new CommentDto(FIRST_COMMENT_ID, NEW_COMMENT_TEXT, bookById1);

        var updatedComment = commentService.update(FIRST_COMMENT_ID, NEW_COMMENT_TEXT, bookById1.getId());
        assertThat(updatedComment).isNotNull().isEqualTo(expectedComment);

        assertThat(updatedComment.getBookDto().getGenreDto()).isEqualTo(expectedComment.getBookDto().getGenreDto());
        assertThat(updatedComment.getBookDto().getAuthorDto()).isEqualTo(expectedComment.getBookDto().getAuthorDto());

        assertThat(commentService.findById(updatedComment.getId()))
                .isEqualTo(updatedComment);

        assertThat(commentService.findAllByBookId(bookById1.getId()))
                .size().isEqualTo(COMMENTS_COUNT_BY_FIRST_ID_BOOK);
    }

    @DisplayName("удалять комментарий по id")
    @Test
    void deleteById() {
        assertThat(commentService.findById(FIRST_COMMENT_ID)).isNotNull();
        commentService.deleteById(1L);
        assertThrows(EntityNotFoundException.class,
                () -> commentService.findById(FIRST_COMMENT_ID));
    }
}
