package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Testing run commands")
@RequiredArgsConstructor
public class Commands {

    private final TestRunnerService testRunnerService;

    private final AppProperties appProperties;

    private final LocalizedIOService localizedIOService;

    @ShellMethod(value = "Run Testing", key = {"run", "start", "r", "s"})
    @SuppressWarnings("unused")
    public void runTest() {
        testRunnerService.run();
    }

    @ShellMethod(value = "Set locale", key = {"set locale","setl"})
    @SuppressWarnings("unused")
    public void setLocale(@ShellOption String locale) {
        if (!appProperties.getEnabledLocales().contains(locale)) {
            localizedIOService.printLine("Invalid locale: " + locale);
            localizedIOService.printLine("Enabled locales: " + appProperties.getEnabledLocales());
            localizedIOService.printLine("Remain locale: " + appProperties.getLocale().toLanguageTag());
        } else {
            appProperties.setLocale(locale);
            localizedIOService.printLine("Current locale: " + locale);
        }
    }

    @ShellMethod(value = "Get locale", key = {"locale"})
    @SuppressWarnings("unused")
    public void getLocale() {
       localizedIOService.printLine("Current locale: " + appProperties.getLocale().toLanguageTag());
    }
}