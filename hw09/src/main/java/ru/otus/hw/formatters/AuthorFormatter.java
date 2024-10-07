package ru.otus.hw.formatters;

import org.springframework.format.Formatter;
import ru.otus.hw.dtos.AuthorDto;

import javax.annotation.Nonnull;
import java.util.Locale;

public class AuthorFormatter implements Formatter<AuthorDto> {

    @Override
    @Nonnull
    public String print(AuthorDto author, @Nonnull Locale locale) {
        return String.valueOf(author.getId());
    }

    @Override
    @Nonnull
    public AuthorDto parse(@Nonnull String id, @Nonnull Locale locale) {
        AuthorDto author = new AuthorDto();
        author.setId(Long.parseLong(id));
        return author;
    }
}