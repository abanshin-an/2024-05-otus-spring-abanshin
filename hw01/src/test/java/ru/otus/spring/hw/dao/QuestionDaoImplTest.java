package ru.otus.spring.hw.dao;

import org.junit.jupiter.api.Test;
import ru.otus.spring.hw.config.AppProperties;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.exceptions.QuestionsLoadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class QuestionDaoImplTest {
    private final AppProperties appProperties = mock(AppProperties.class);

    private final QuestionDao questionDao=new QuestionDaoImpl(appProperties);
    @Test
    void findAll() {
        doReturn("questions.csv").when(appProperties).getQuestionsFileName();
        List<Question> questions = questionDao.findAll();
        assertNotNull(questions);
        assertThat(questions).hasSize(3);
        assertThat(questions.get(0)).hasFieldOrPropertyWithValue("questionText", "Is there life on Mars?");
    }
    @Test
    void testQuestionLoadException() {
        doReturn("questions.---").when(appProperties).getQuestionsFileName();
        assertThrows(QuestionsLoadException.class,()->questionDao.findAll());
    }
}