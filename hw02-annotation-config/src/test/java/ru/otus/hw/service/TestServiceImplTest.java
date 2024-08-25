package ru.otus.hw.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class TestServiceImplTest {

    private final AppProperties appProperties = new AppProperties(1, "questions-test.csv");

    private final QuestionDao questionDao = new CsvQuestionDao(appProperties);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private final InputStream originalIn = System.in;

    private final PrintStream originalOut = System.out;

    private IOService ioService;

    private TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);


    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream("1\n3".getBytes()));
        ioService = new StreamsIOService(System.out, System.in);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void executeTestFor() {
        testService.executeTestFor(new Student("firstName", "lastName"));
        var s = outContent.toString();
        assertThat(s).isEqualToNormalizingNewlines(expectedString());
    }

    private String expectedString() {
        return
"""
                        
Please answer the questions below

Can nothing exist?
1. Exist
2. Not exist
3. Everything has already been stolen before us
Please input your answer
\u001B[31mWrong answer\u001B[0m
Is our Universe real?
1. Real
2. Not real
3. This is a matrix
Please input your answer
\u001B[32mCorrect answer\u001B[0m
""";
    }
}