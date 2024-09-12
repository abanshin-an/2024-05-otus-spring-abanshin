package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private static final String FIRST_TITLE_FOR_BOOK = "BookTitle_1";
    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    private BookDto bookByFirstId;

    @BeforeEach
    void setUp() {
        bookByFirstId = new BookDto(FIRST_BOOK_ID, FIRST_TITLE_FOR_BOOK,
                new AuthorDto(1L, "Author_1"),
                new GenreDto(1L, "Genre_1"));
    }

    @Test
    void indexPage() throws Exception {
        List<BookDto> books = List.of(bookByFirstId);
        when(bookService.findAll()).thenReturn(books);

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("index"));
    }

    @Test
    void editBookPage() throws Exception {
        var genres = Collections.singletonList(new GenreDto(1L, "Genre 1"));
        var authors = Collections.singletonList(new AuthorDto(1L, "Author 1"));

        when(bookService.findById(anyLong())).thenReturn(bookByFirstId);
        when(genreService.findAll()).thenReturn(genres);
        when(authorService.findAll()).thenReturn(authors);

        mvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book", "genres", "authors"))
                .andExpect(model().attribute("book", bookByFirstId))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("authors", authors))
                .andExpect(view().name("book/editBook"));
    }

    @Test
    void updateBook() throws Exception {
        when(bookService.update(anyLong(), any(BookDto.class)))
                .thenReturn(new BookDto(1L, "Updated Book",
                        new AuthorDto(1L, "Author 1"),
                        List.of(new GenreDto(1L, "Genre 1"))));
        mvc.perform(post("/book/1")
                        .param("title", "Updated Book")
                        .param("authorDto.id", "1")
                        .param("genreDto.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void newBookPage() throws Exception {
        var genres = Collections.singletonList(new GenreDto(1L, "Genre 1"));
        var authors = Collections.singletonList(new AuthorDto(1L, "Author 1"));

        when(genreService.findAll()).thenReturn(genres);
        when(authorService.findAll()).thenReturn(authors);

        mvc.perform(get("/book/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book", "genres", "authors"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres))
                .andExpect(view().name("book/editBook"));
    }

    @Test
    void addNewBook() throws Exception {
        when(bookService.insert(any(BookDto.class)))
                .thenReturn(bookByFirstId);

        mvc.perform(post("/book/new")
                        .param("title", bookByFirstId.getTitle())
                        .param("authorDto.id", String.valueOf(bookByFirstId.getAuthorDto().getId()))
                        .param("genreDto.id", String.valueOf(bookByFirstId.getGenreDto().getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteById(anyLong());

        mvc.perform(post("/deleteBook")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
