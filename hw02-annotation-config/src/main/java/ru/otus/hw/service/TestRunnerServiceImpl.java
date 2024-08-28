package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.IoConfig;
import ru.otus.hw.exceptions.AnswerException;
import ru.otus.hw.exceptions.QuestionReadException;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final IOService ioService;

    private final IoConfig ioConfig;

    @Override
    public void run() {
        try {
            var student = studentService.determineCurrentStudent();
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (QuestionReadException e) {
            ioService.printFormattedLine("Error reading questions");
            ioService.printLine("Sorry. Please call support.");
        } catch (AnswerException e) {
            ioService.printFormattedLine("You can't answer in %d attempts", ioConfig.getMaxAnswerAttempts());
            ioService.printLine("Sorry. You fail test.");
        }
    }
}
