package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

import java.util.List;

@ShellComponent(value = "Testing run commands")
@RequiredArgsConstructor
public class ApplicationRun {

    private final TestRunnerService testRunnerService;

    private final AppProperties appProperties;

    private final LocalizedIOService localizedIOService;

    private final List<String> enabledLocales = List.of("ru-RU", "en-US");

    @ShellMethod(value = "Run Testing", key = {"run", "start", "r", "s"})
    @SuppressWarnings("unused")
    public void runTest() {
        testRunnerService.run();
    }

    @ShellMethod(value = "Set locale", key = {"set locale","setl"})
    @SuppressWarnings("unused")
    public void setLocale(@ShellOption String locale) {
        if (!enabledLocales.contains(locale)) {
            localizedIOService.printLine("Invalid locale: " + locale);
            localizedIOService.printLine("Enabled locales: " + enabledLocales);
            localizedIOService.printLine("Remain locale: " + appProperties.getLocale().toLanguageTag());
        } else {
            appProperties.setLocale(locale);
        }
    }

    @ShellMethod(value = "Get locale", key = {"locale"})
    @SuppressWarnings("unused")
    public void getLocale() {
       localizedIOService.printLine("Current locale: " + appProperties.getLocale().toLanguageTag());
    }
}