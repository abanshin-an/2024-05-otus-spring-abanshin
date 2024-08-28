package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.IoConfig;
import ru.otus.hw.exceptions.AnswerException;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.formatters.Tag;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final LocalizedIOService ioService;

    private final IoConfig ioConfig;

    @Override
    public void run() {
        try {
            var student = studentService.determineCurrentStudent();
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (QuestionReadException e) {
            ioService.printTagFormattedLineLocalized(Tag.ANSWER_WRONG,"IoService.fail.reading");
            ioService.printTagFormattedLineLocalized(Tag.ANSWER_WRONG,"IoService.fail.support");
        } catch (AnswerException e) {
            ioService.printTagFormattedLineLocalized(Tag.ANSWER_WRONG,"Answer.attempts",
                    ioConfig.getMaxAnswerAttempts());
            ioService.printTagFormattedLineLocalized(Tag.ANSWER_WRONG, "ResultService.fail.test");
        }
    }
}