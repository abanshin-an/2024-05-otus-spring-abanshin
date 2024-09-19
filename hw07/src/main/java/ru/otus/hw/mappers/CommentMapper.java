package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentDto modelToDto(Comment model);

    List<CommentDto> modelsToDto(List<Comment> models);

}
