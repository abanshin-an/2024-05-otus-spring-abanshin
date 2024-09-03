package ru.otus.hw.service;


import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.mockito.Mockito.*;

class TestServiceImplTest {


    private final QuestionDao questionDao = mock(CsvQuestionDao.class);

    private final IOService ioService = mock(IOService.class);

    private final TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);

    private final Student student = new Student("firstName", "lastName");

    @Test
    void executeTestFor() {
        var expectedQuestions = getQuestions();
        doReturn(getQuestions()).when(questionDao).findAll();
        when(ioService.readIntForRangeWithPrompt(eq(1), eq(3), anyString(), anyString())).thenReturn(1,3);
        testService.executeTestFor(student);

        verify(ioService, times(2)).readIntForRangeWithPrompt(eq(1), eq(3), anyString(), anyString());
        verify(ioService, times(1)).printFormattedLine(eq(expectedQuestions.get(0).text()));
        verify(ioService, times(1)).printFormattedLine(eq(expectedQuestions.get(1).text()));
        var expectedAnswers = expectedQuestions.get(0).answers();
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(1), eq(expectedAnswers.get(0).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(2), eq(expectedAnswers.get(1).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(3), eq(expectedAnswers.get(2).text()));
        verify(ioService, times(1)).printFormattedLine(eq("\u001B[31mWrong answer\u001B[0m"));
        expectedAnswers = expectedQuestions.get(1).answers();
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(1), eq(expectedAnswers.get(0).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(2), eq(expectedAnswers.get(1).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(3), eq(expectedAnswers.get(2).text()));
        verify(ioService, times(1)).printFormattedLine(eq("\u001B[32mCorrect answer\u001B[0m"));
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