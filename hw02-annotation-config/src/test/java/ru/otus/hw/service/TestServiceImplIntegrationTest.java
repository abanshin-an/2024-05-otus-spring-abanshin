package ru.otus.hw.service;


import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TestServiceImplIntegrationTest {

    private final AppProperties appProperties = new AppProperties(1, "questions-test.csv", 5);

    private final QuestionDao questionDao = new CsvQuestionDao(appProperties);

    private final IOService ioService = mock(IOService.class);

    private final TestServiceImpl testService = spy(new TestServiceImpl(ioService, questionDao));

    private final Student student = new Student("firstName", "lastName");

    @Test
    void executeTestForMissingQuestions() {
        AppProperties appProperties1 = new AppProperties(1, "questions-test-missing.csv", 5);
        QuestionDao questionDao1 = new CsvQuestionDao(appProperties1);
        TestServiceImpl testService1 = new TestServiceImpl(ioService, questionDao1);
        assertThrows(QuestionReadException.class,()->testService1.executeTestFor(student));
    }

    @Test
    void executeTestFor() {
        var expectedQuestions = getQuestions();
        testService.executeTestFor(student);
        verify(ioService, times(2)).readIntForRangeWithPrompt(eq(1), eq(3), anyString(), anyString());
        verify(ioService, times(1)).printFormattedLine(eq(expectedQuestions.get(0).text()));
        verify(ioService, times(1)).printFormattedLine(eq(expectedQuestions.get(1).text()));
        var expectedAnswers = expectedQuestions.get(0).answers();
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(1), eq(expectedAnswers.get(0).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(2), eq(expectedAnswers.get(1).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(3), eq(expectedAnswers.get(2).text()));
        expectedAnswers = expectedQuestions.get(1).answers();
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(1), eq(expectedAnswers.get(0).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(2), eq(expectedAnswers.get(1).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(3), eq(expectedAnswers.get(2).text()));
    }

    private List<Question> getQuestions() {
        return List.of(
                new Question("Can nothing exist?", List.of(
                        new Answer("Exist", false),
                        new Answer("Not exist", false),
                        new Answer("Everything has already been stolen before us", true)
                )),
                new Question("Is our Universe real?", List.of(
                        new Answer("Real", false),
                        new Answer("Not real", false),
                        new Answer("This is a matrix", true)))
        );

    }
}