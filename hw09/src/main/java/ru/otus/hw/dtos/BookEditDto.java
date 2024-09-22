package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookEditDto extends BookDto {

    private List<Long> genreIds = List.of();

    public BookEditDto(BookDto bookDto) {
        setId(bookDto.getId());
        setAuthor(bookDto.getAuthor());
        setTitle(bookDto.getTitle());
        setGenres(bookDto.getGenres());
    }

    @Override
    public void setGenres(@NotNull(message = "Genre must be selected") List<GenreDto> genres) {
        super.setGenres(genres);
        genreIds = genres.stream().map(GenreDto::getId).toList();
    }
}
