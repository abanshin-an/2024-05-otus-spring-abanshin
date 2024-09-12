package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.models.Genre;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenreMapper {
    
    GenreDto modelToDto(Genre model);
    
    List<GenreDto> modelsToDto(List<Genre> models);
    
}
