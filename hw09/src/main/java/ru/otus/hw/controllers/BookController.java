package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private static final String REDIRECT = "redirect:/";

    private static final String BOOK_BY_ID = "/book/{id}";

    private static final String BOOK = "/book/";

    private static final String BOOK_NEW = "/book/new";

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/")
    public String indexPage(Model model) {
        List<BookDto> allBooks = bookService.findAll();
        model.addAttribute("books", allBooks);
        return "index";
    }

    @GetMapping(BOOK_BY_ID)
    public String editBookPage(@PathVariable("id") Long id, Model model) {
        BookDto bookDto = bookService.findById(id).orElse(new BookDto());
        model.addAttribute("book", bookDto);
        return setupModel(model);
    }

    @PostMapping(BOOK)
    public String updateBook(@Valid @ModelAttribute("book") BookDto bookDto, @ModelAttribute("genres") String genreIds,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return setupModel(model);
        }
        bookService.update(bookDto);
        return REDIRECT;
    }

    @GetMapping(BOOK_NEW)
    public String newBookPage(Model model) {
        model.addAttribute("book", new BookDto());
        return setupModel(model);
    }

    @PostMapping(BOOK_NEW)
    public String addNewBook(@Valid @ModelAttribute("book") BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return setupModel(model);
        }
        bookService.insert(bookDto);
        return REDIRECT;
    }

    @DeleteMapping("/book/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return REDIRECT;
    }

    private String setupModel(Model model) {
        model.addAttribute("allGenres", genreService.findAll());
        model.addAttribute("author", authorService.findAll());
        return "book/edit";
    }
}
