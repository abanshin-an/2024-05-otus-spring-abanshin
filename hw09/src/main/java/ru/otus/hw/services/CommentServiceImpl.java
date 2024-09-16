package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Override
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(commentMapper::modelToDto);
    }

    @Override
    public List<CommentDto> findAllByBookId(long bookId) {
        return commentMapper.modelsToDto(commentRepository.findAllByBookId(bookId));
    }

    @Transactional
    @Override
    public CommentDto insert(String content, long bookId) {
        var book = getBookById(bookId);
        var comment =  new Comment(0, content, book);
        return commentMapper.modelToDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(long id, String content, long bookId) {
        var book = getBookById(bookId);
        var comment = getCommentById(id);
        comment.setContent(content);
        comment.setBook(book);
        return commentMapper.modelToDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.findById(id).ifPresent(commentRepository::delete);
    }

    private Comment getCommentById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
    }

    private Book getBookById(long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
    }

}
