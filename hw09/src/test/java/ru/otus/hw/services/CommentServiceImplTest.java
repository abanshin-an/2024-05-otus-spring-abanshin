package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapperImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Сервис для работы с комментариями должен")
@DataJpaTest
@Transactional(propagation = Propagation.NEVER)
@Import({CommentServiceImpl.class, CommentMapperImpl.class})
class CommentServiceImplTest {

    private static final int COMMENTS_COUNT_BY_ID_BOOK = 2;
    private static final long COMMENT_ID = 1L;
    private static final long BOOK_ID = 1L;
    private static final String COMMENT_CONTENT = "Comment_1_1";
    private static final String COMMENT_CONTENT_1 = "NEW COMMENT";

    @Autowired
    private CommentService commentService;

    private BookDto bookById1;

    private CommentDto commentForBookWithId1;

    @BeforeEach
    void setUp() {
        bookById1 = new BookDto(BOOK_ID, "BookTitle_1",
                new AuthorDto(1L, "Author_1"),
                List.of(new GenreDto(1L, "Genre_1")));
        commentForBookWithId1 = new CommentDto(COMMENT_ID, COMMENT_CONTENT, bookById1);
    }

    @DisplayName("вернуть комментарий по его id")
    @Test
    void findById() {
        var comment = commentService.findById(COMMENT_ID).orElseThrow(()->new EntityNotFoundException("Not found comment with Id %d".formatted(COMMENT_ID)));

        assertThat(comment).isEqualTo(commentForBookWithId1);
        assertThat(comment.getBook()).isEqualTo(bookById1);
        assertThat(comment.getBook().getAuthor()).isEqualTo(bookById1.getAuthor());
        assertThat(comment.getBook().getGenres()).isEqualTo(bookById1.getGenres());
    }

    @DisplayName("вернуть все комментарии для книги по ее id")
    @Test
    void findAllByBookId() {
        var comments = commentService.findAllByBookId(BOOK_ID);

        assertThat(comments.size()).isEqualTo(COMMENTS_COUNT_BY_ID_BOOK);
        assertThat(comments.get(0)).isEqualTo(commentForBookWithId1);
        assertThat(comments.get(0).getBook()).isEqualTo(bookById1);
    }

    @DisplayName("сохранить новый комментарий")
    @Test
    void insert() {
        var expectedComment = new CommentDto(0, COMMENT_CONTENT_1, bookById1);

        var savedComment = commentService.insert(COMMENT_CONTENT_1, bookById1.getId());
        assertThat(savedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .hasFieldOrPropertyWithValue("text", expectedComment.getContent())
                .hasFieldOrPropertyWithValue("bookDto", expectedComment.getBook());

        assertThat(savedComment.getBook().getGenres()).isEqualTo(bookById1.getGenres());
        assertThat(savedComment.getBook().getAuthor()).isEqualTo(bookById1.getAuthor());

        assertThat(commentService.findById(savedComment.getId()))
                .isEqualTo(savedComment);

        assertThat(commentService.findAllByBookId(bookById1.getId()))
                .size().isEqualTo(COMMENTS_COUNT_BY_ID_BOOK + 1);
    }

    @DisplayName("сохранять измененный комментарий")
    @Test
    void update() {
        var expectedComment = new CommentDto(COMMENT_ID, COMMENT_CONTENT_1, bookById1);

        var updatedComment = commentService.update(COMMENT_ID, COMMENT_CONTENT_1, bookById1.getId());
        assertThat(updatedComment).isNotNull().isEqualTo(expectedComment);

        assertThat(updatedComment.getBook().getGenres()).isEqualTo(expectedComment.getBook().getGenres());
        assertThat(updatedComment.getBook().getAuthor()).isEqualTo(expectedComment.getBook().getAuthor());

        assertThat(commentService.findById(updatedComment.getId()))
                .isEqualTo(updatedComment);

        assertThat(commentService.findAllByBookId(bookById1.getId()))
                .size().isEqualTo(COMMENTS_COUNT_BY_ID_BOOK);
    }

    @DisplayName("удалять комментарий по id")
    @Test
    void deleteById() {
        assertThat(commentService.findById(COMMENT_ID)).isNotNull();
        commentService.deleteById(1L);
        assertThrows(EntityNotFoundException.class,
                () -> commentService.findById(COMMENT_ID));
    }
}
