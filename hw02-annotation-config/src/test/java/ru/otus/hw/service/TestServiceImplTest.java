package ru.otus.hw.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

class TestServiceImplTest {

    private final AppProperties appProperties = new AppProperties(1, "questions-test.csv");

    private final QuestionDao questionDao = new CsvQuestionDao(appProperties);

    private final IOService ioService = new StreamsIOService(System.out, System.in);

    private final TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private final InputStream originalIn = System.in;

    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream("7\n2\n3".getBytes()));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void executeTestFor() {
        testService.executeTestFor(new Student("firstName", "lastName"));
    }

    private List<Question> getQuestionList() {
        return List.of(new Question("Is there life on Mars?",
                List.of(new Answer("Science doesn't know this yet",true),
                        new Answer("Certainly. The red UFO is from Mars. And green is from Venus",false),
                        new Answer("Absolutely not",false))));
    }
}