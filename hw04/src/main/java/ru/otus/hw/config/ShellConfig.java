package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.Availability;
import org.springframework.shell.AvailabilityProvider;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.LocalizedIOService;

@Component
public AvailabilityProvider showResultCommandAvailability(
        @Qualifier("testCommandsServiceImpl") TestCommandsServiceImpl testRunnerService,
        LocalizedIOService localizedIOService) {

    return () -> {
        TestResult result = testRunnerService.getResult();
        return result != null ? Availability.available() : Availability.unavailable(
                localizedIOService.getMessage("AvailabilityProvider.showResult.unavailable"));
    };
}