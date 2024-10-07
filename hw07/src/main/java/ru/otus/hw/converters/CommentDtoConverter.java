package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.CommentDto;

@Component
public class CommentDtoConverter {
    public String convert(CommentDto comment) {
        return "Id: %d, title: %s".formatted(comment.getId(), comment.getContent());
    }
}
