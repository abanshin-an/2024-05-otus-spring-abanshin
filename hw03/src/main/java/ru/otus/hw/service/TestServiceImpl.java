package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.AnswerException;
import ru.otus.hw.formatters.Tag;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {


    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printTagFormattedLine(Tag.TITLE, ioService.getMessage("TestService.answer.the.questions"));
        ioService.printLine("");

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
            ioService.printFormattedLine("%d. %s", i, answer.text());
        }
        int studentAnswer = getStudentAnswer(question);
        isAnswerValid = studentAnswer == rightAnswer;
        if (isAnswerValid) {
            ioService.printTagFormattedLine(Tag.ANSWER_CORRECT, ioService.getMessage("Answer.correct"));
        } else {
            ioService.printTagFormattedLine(Tag.ANSWER_WRONG, ioService.getMessage("Answer.wrong"));
        }
        return isAnswerValid;
    }

    private int getStudentAnswer(Question question) {
        int studentAnswer;
        try {
            studentAnswer = ioService.readIntForRangeWithPrompt(1, question.answers().size(),
                    ioService.getMessage("Answer.input"),
                    ioService.getMessage("Answer.range", question.answers().size()));
        } catch (IllegalArgumentException e) {
            throw new AnswerException("Error during reading answer");
        }
        return studentAnswer;
    }
}
