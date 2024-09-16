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
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/book/{bookId}/comments")
public class CommentController {

    private final CommentService commentService;

    private final BookService bookService;

    @GetMapping
    public String viewComments(@PathVariable("bookId") Long bookId, Model model) {
        List<CommentDto> comments = commentService.findAllByBookId(bookId);
        String title = bookService.findById(bookId).orElse(new BookDto()).getTitle();
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", bookId);
        model.addAttribute("title", title);
        model.addAttribute("comment", new CommentDto());
        return "comment/comments";
    }

    @PostMapping
    public String addComment(@PathVariable("bookId") Long bookId,
                             @Valid @ModelAttribute("comment") CommentDto commentDto,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("comments", commentService.findAllByBookId(bookId));
            model.addAttribute("bookId", bookId);
            return "comment/comments";
        }
        commentService.insert(commentDto.getContent(), bookId);
        return "redirect:/book/" + bookId + "/comments";
    }

    @GetMapping("/{id}/edit")
    public String editCommentPage(@PathVariable("bookId") Long bookId,
                                  @PathVariable("id") Long id,
                                  Model model) {
        CommentDto comment = commentService.findById(id).orElse(new CommentDto());
        model.addAttribute("comment", comment);
        model.addAttribute("bookId", bookId);
        return "comment/editComment";
    }

    @PostMapping("/{id}/edit")
    public String updateComment(@PathVariable("bookId") Long bookId,
                                @PathVariable("id") Long id,
                                @Valid @ModelAttribute CommentDto commentDto,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "comment/editComment";
        }
        commentService.update(id, commentDto.getContent(), bookId);
        return "redirect:/book/" + bookId + "/comments";
    }

    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable("bookId") Long bookId,
                                @PathVariable("id") Long id) {
        commentService.deleteById(id);
        return "redirect:/book/" + bookId + "/comments";
    }
}
