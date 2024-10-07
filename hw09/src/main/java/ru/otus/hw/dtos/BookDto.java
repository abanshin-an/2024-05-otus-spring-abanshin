package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private long id;

    @NotBlank(message = "Title must be not empty")
    @Size(min = 2, max = 50, message = "The title size is limited to 50 characters")
    private String title;

    @NotNull(message = "Author must be selected")
    private AuthorDto author = new AuthorDto();

    @NotNull(message = "Author must be selected")
    private List<GenreDto> genres = List.of();

}
