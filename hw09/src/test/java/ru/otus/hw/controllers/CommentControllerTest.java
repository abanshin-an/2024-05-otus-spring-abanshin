package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Disabled
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    private static final long BOOK_ID = 1L;
    private static final long COMMENT_ID = 1L;

    private static final String COMMENT_TEXT = "Great book!";
    private static final String NEW_COMMENT_TEXT = "new comment";
    private static final String TITLE_FOR_BOOK = "BookTitle_1";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    private List<CommentDto> comments;

    private BookDto bookByFirstId;

    @BeforeEach
    void setUp() {
        bookByFirstId = new BookDto(BOOK_ID, TITLE_FOR_BOOK,
                new AuthorDto(1L, "Author_1"),
                List.of(new GenreDto(1L, "Genre_1")));
        comments = List.of(new CommentDto(COMMENT_ID, COMMENT_TEXT, bookByFirstId));
    }

    @Test
    void viewComments() throws Exception {
        when(commentService.findAllByBookId(BOOK_ID)).thenReturn(comments);
        when(bookService.findById(BOOK_ID))
                .thenReturn(Optional.of(bookByFirstId));

        mvc.perform(get("/book/{bookId}/comments", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("comment/comments"))
                .andExpect(model().attributeExists("comments", "bookId", "title", "comment"))
                .andExpect(model().attribute("bookId", bookByFirstId.getId()))
                .andExpect(model().attribute("title", bookByFirstId.getTitle()));
    }

    @Test
    void addComment() throws Exception {

        mvc.perform(post("/book/{bookId}/comments", BOOK_ID)
                        .param("text", NEW_COMMENT_TEXT))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/" + BOOK_ID + "/comments"));

        verify(commentService, times(1)).insert(NEW_COMMENT_TEXT, BOOK_ID);
    }

    @Test
    void editCommentPage() throws Exception {
        when(commentService.findById(COMMENT_ID)).thenReturn(Optional.of(comments.get(0)));

        mvc.perform(get("/book/{bookId}/comments/{id}/edit", BOOK_ID, COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("comment/editComment"))
                .andExpect(model().attributeExists("comment", "bookId"))
                .andExpect(model().attribute("comment", comments.get(0)))
                .andExpect(model().attribute("bookId", BOOK_ID));
    }

    @Test
    void updateComment() throws Exception {
        mvc.perform(post("/book/{bookId}/comments/{id}/edit", BOOK_ID, COMMENT_ID)
                        .param("text", NEW_COMMENT_TEXT))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/" + BOOK_ID + "/comments"));

        verify(commentService, times(1)).update(COMMENT_ID, NEW_COMMENT_TEXT, BOOK_ID);
    }

    @Test
    void deleteComment() throws Exception {
        mvc.perform(post("/book/{bookId}/comments/{id}/delete", BOOK_ID, COMMENT_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/" + BOOK_ID + "/comments"));

        verify(commentService, times(1)).deleteById(COMMENT_ID);
    }
}
