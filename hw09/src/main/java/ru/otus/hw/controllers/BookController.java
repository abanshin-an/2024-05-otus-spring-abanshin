package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookEditDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private static final String REDIRECT_URL = "redirect:/";

    private static final String BOOK_BY_ID_URL = "/book/{id}";

    private static final String BOOK_URL = "/book/";

    private static final String BOOK_NEW_URL = "/book/new";

    private static final String BOOK = "book";

    private static final String ID = "id";

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/")
    public String indexPage(Model model) {
        List<BookDto> allBooks = bookService.findAll();
        model.addAttribute("books", allBooks);
        return "index";
    }

    @GetMapping(BOOK_BY_ID_URL)
    public String editBookPage(@PathVariable(ID) Long id, Model model) {
        BookDto bookDto = bookService.findById(id).orElse(new BookEditDto());
        model.addAttribute(BOOK, new BookEditDto(bookDto));
        return setupModel(model);
    }

    @PostMapping(BOOK_URL)
    public String updateBook(@ModelAttribute(BOOK) BookEditDto bookEditDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return setupModel(model);
        }
        fixGenres(bookEditDto);
        bookService.update(bookEditDto);
        return REDIRECT_URL;
    }

    private void fixGenres(BookEditDto bookEditDto) {
        List<GenreDto> genreDtos = bookEditDto.getGenreIds().stream().map(id -> new GenreDto(id, null)).toList();
        bookEditDto.setGenres(genreDtos);
    }

    @GetMapping(BOOK_NEW_URL)
    public String newBookPage(Model model) {
        model.addAttribute(BOOK, new BookEditDto());
        return setupModel(model);
    }

    @PostMapping(BOOK_NEW_URL)
    public String addNewBook(@ModelAttribute(BOOK) BookEditDto bookEditDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return setupModel(model);
        }
        fixGenres(bookEditDto);
        bookService.insert(bookEditDto);
        return REDIRECT_URL;
    }

    @PostMapping("/book/{id}/delete")
    public String deleteBook(@PathVariable(ID) Long id) {
        bookService.deleteById(id);
        return REDIRECT_URL;
    }

    private String setupModel(Model model) {
        model.addAttribute("allGenres", genreService.findAll());
        model.addAttribute("allAuthors", authorService.findAll());
        return "book/edit";
    }
}
