package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private long id;

    @NotBlank(message = "Text of comment must be not empty")
    @Size(max = 1000, message = "The title size is limited to 1000 characters")
    private String content;

    private BookDto book;
}
