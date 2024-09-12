package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/")
    public String indexPage(Model model) {
        List<BookDto> allBooks = bookService.findAll();
        model.addAttribute("books", allBooks);
        return "index";
    }

    @GetMapping("/book/{id}")
    public String editBookPage(@PathVariable("id") Long id, Model model) {
        BookDto bookDto = bookService.findById(id).orElse(new BookDto());
        model.addAttribute("book", bookDto);
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "book/editBook";
    }

    @PostMapping("/book/{id}")
    public String updateBook(@PathVariable("id") Long id, @Valid @ModelAttribute("book") BookDto bookDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("authors", authorService.findAll());
            return "book/editBook";
        }
        bookService.update(bookDto);
        return "redirect:/";
    }

    @GetMapping("/book/new")
    public String newBookPage(Model model) {
        model.addAttribute("book", new BookDto());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "book/editBook";
    }

    @PostMapping("/book/new")
    public String addNewBook(@Valid @ModelAttribute("book") BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("authors", authorService.findAll());
            return "book/editBook";
        }
        bookService.insert(bookDto);
        return "redirect:/";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
