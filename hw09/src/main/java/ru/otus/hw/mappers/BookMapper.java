package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.models.Book;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    BookDto modelToDto(Book model);

    List<BookDto> modelsToDto(List<Book> models);

    Book modelFromDto(BookDto dto);
}
