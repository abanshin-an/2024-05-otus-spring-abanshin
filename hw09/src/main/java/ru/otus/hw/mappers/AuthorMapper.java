package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.models.Author;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorMapper {

    AuthorDto modelToDto(Author model);

    List<AuthorDto> modelsToDto(List<Author> models);

}
