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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private static final String TITLE_FOR_BOOK = "BookTitle_1";
    private static final long BOOK_ID = 1L;

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
        bookByFirstId = new BookDto(BOOK_ID, TITLE_FOR_BOOK,
                new AuthorDto(1L, "Author_1"),
                List.of(new GenreDto(1L, "Genre_1")));
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

        when(bookService.findById(anyLong())).thenReturn(Optional.of(bookByFirstId));
        when(genreService.findAll()).thenReturn(genres);
        when(authorService.findAll()).thenReturn(authors);

        mvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book", "allGenres", "author"))
                .andExpect(model().attribute("book", bookByFirstId))
                .andExpect(model().attribute("allGenres", genres))
                .andExpect(model().attribute("author", authors))
                .andExpect(view().name("book/edit"));
    }

    @Test
    void updateBook() throws Exception {
        var book = new BookDto(1L, "Updated Book",
                new AuthorDto(1L, "Author 1"),
                List.of(new GenreDto(1L, "Genre 1")));
        mvc.perform(post("/book/")
                          .flashAttr("book", book)
                )
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
                .andExpect(model().attributeExists("book", "allGenres", "author"))
                .andExpect(model().attribute("author", authors))
                .andExpect(model().attribute("allGenres", genres))
                .andExpect(view().name("book/edit"));
    }

    @Test
    void addNewBook() throws Exception {
        when(bookService.insert(any(BookDto.class)))
                .thenReturn(bookByFirstId);

        mvc.perform(post("/book/new")
                        .param("title", bookByFirstId.getTitle())
                        .param("author.id", String.valueOf(bookByFirstId.getAuthor().getId()))
                        .param("genres.id", "1","2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteById(anyLong());

        mvc.perform(delete("/book/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}
