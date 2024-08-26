package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_RED = "\u001B[31m";

    private static final String ANSI_GREEN = "\u001B[32m";

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            boolean isAnswerValid = askQuestion(question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean askQuestion(Question question) {
        var isAnswerValid = false;
        ioService.printFormattedLine(question.text());
        int rightAnswer = 0;
        for (int i = 1; i <= question.answers().size(); i++) {
            var answer = question.answers().get(i - 1);
            if (answer.isCorrect()) {
                rightAnswer = i;
            }
            ioService.printFormattedLine("%d. %s",i , answer.text());
        }
        var studentAnswer = ioService.readIntForRangeWithPrompt(1, question.answers().size(),
                "Please input your answer",
                "Please input number from 1 to " + (question.answers().size()));
        isAnswerValid = studentAnswer == rightAnswer;
        ioService.printFormattedLine((isAnswerValid ? ANSI_GREEN + "Correct answer" :
                ANSI_RED + "Wrong answer") + ANSI_RESET);
        return isAnswerValid;
    }
}
