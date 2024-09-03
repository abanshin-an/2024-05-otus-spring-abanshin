package ru.otus.hw.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.formatters.AnsiFormatter;
import ru.otus.hw.formatters.Tag;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class TestServiceImplTest {

    @MockBean
    private QuestionDao questionDao;

    @MockBean
    private LocalizedIOServiceImpl ioService;

    @MockBean
    private AnsiFormatter ansiFormatter;

    @Autowired
    private TestServiceImpl testService;

    private final Student student = new Student("firstName", "lastName");

    @Test
    void executeTestFor() {
        var expectedQuestions = getQuestions();
        doReturn(getQuestions()).when(questionDao).findAll();
        doReturn("").when(ioService).getMessage(anyString());
        doReturn("").when(ioService).getMessage(anyString(),any());
        doReturn(1,3).when(ioService).readIntForRangeWithPrompt(eq(1), eq(3), anyString(), anyString());
        doReturn("").when(ioService).getMessage("Answer.wrong");
        testService.executeTestFor(student);

        verify(ioService, times(2)).readIntForRangeWithPrompt(eq(1), eq(3), anyString(), anyString());
        verify(ioService, times(1)).printFormattedLine(eq(expectedQuestions.get(0).text()));
        verify(ioService, times(1)).printFormattedLine(eq(expectedQuestions.get(1).text()));
        var expectedAnswers = expectedQuestions.get(0).answers();
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(1), eq(expectedAnswers.get(0).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(2), eq(expectedAnswers.get(1).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(3), eq(expectedAnswers.get(2).text()));
        verify(ioService, times(1)).printTagFormattedLine(eq(Tag.ANSWER_WRONG),anyString());
        expectedAnswers = expectedQuestions.get(1).answers();
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(1), eq(expectedAnswers.get(0).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(2), eq(expectedAnswers.get(1).text()));
        verify(ioService, times(1)).printFormattedLine(anyString(), eq(3), eq(expectedAnswers.get(2).text()));
        verify(ioService, times(1)).printTagFormattedLine(eq(Tag.ANSWER_CORRECT),anyString());
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