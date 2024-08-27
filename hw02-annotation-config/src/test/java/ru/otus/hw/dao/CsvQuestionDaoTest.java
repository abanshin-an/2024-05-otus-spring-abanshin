package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvQuestionDaoTest {

    @Test
    void findAllTest() {
        AppProperties appProperties = new AppProperties(1, "questions-test.csv", 5);
        CsvQuestionDao csvQuestionDao =  new CsvQuestionDao(appProperties);
        var questions = csvQuestionDao.findAll();
        assertThat(questions).isNotNull().hasSize(2);
        assertThat(questions).isEqualTo(getQuestions());
    }

    @ParameterizedTest
    @ValueSource(strings = { "questions-test-missing.csv", "questions-test-bad1.csv", "questions-test-bad2.csv" ,
            "questions-test-bad3.csv"})
    void findAllExceptionTest(String argument) {
        AppProperties appProperties = new AppProperties(1, argument, 5);
        CsvQuestionDao csvQuestionDao =  new CsvQuestionDao(appProperties);
        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);
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