package ru.otus.hw.config;

import java.util.Locale;
import java.util.Set;

public interface LocaleConfig {
    Locale getLocale();

    Set<String> getEnabledLocales();
}
