package ru.otus.hw.service;


import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.formatters.AnswerFormatter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class TestServiceImplTest {

    private final QuestionDao questionDao = mock(QuestionDao.class);

    private final AnswerFormatter answerFormatter = mock(AnswerFormatter.class);

    private final IOService ioService = mock(StreamsIOService.class);

    private final TestServiceImpl testService = new TestServiceImpl(ioService, questionDao, answerFormatter);

    @Test
    void executeTest() {
        doReturn(getQuestionList()).when(questionDao).findAll();
        doReturn("- Absolutely not").when(answerFormatter).format(any());
        testService.executeTest();
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
                verify(ioService,times(2)).printFormattedLine(argument.capture());
        var result=argument.getValue();
        assertThat(result).contains("- Absolutely not");
    }

    private List<Question> getQuestionList() {
        return List.of(new Question("Is there life on Mars?",
                List.of(new Answer("Science doesn't know this yet",true),
                        new Answer("Certainly. The red UFO is from Mars. And green is from Venus",false),
                        new Answer("Absolutely not",false))));
    }
}