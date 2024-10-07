package ru.otus.hw.formatters;

import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;
import ru.otus.hw.dtos.GenreDto;

import javax.annotation.Nonnull;

@Slf4j
@Nonnull
public class GenreFormatter implements Formatter<GenreDto> {

        @Override
        @Nonnull
        public String print(@Nonnull GenreDto genre, @Nonnull Locale locale) {
            log.info("print genre: {}", genre);
            return String.valueOf(genre.getId());
        }

        @Override
        @Nonnull
        public GenreDto parse(@Nonnull String id, @Nonnull Locale locale) {
            log.info("parse genre: {}", id);
            GenreDto genre = new GenreDto();
            genre.setId(Long.parseLong(id));
            return genre;
        }
}