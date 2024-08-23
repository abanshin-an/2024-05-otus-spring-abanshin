package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
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
            executeQuestion(question, testResult);
        }
        return testResult;
    }

    private void executeQuestion(Question question, TestResult testResult) {
        var isAnswerValid = false; // Задать вопрос, получить ответ
        ioService.printTagFormattedLine(Tag.QUESTION, question.text());
        int i = 1;
        int rightAnswer = 0;
        for (var answer : question.answers()) {
            if (answer.isCorrect()) {
                rightAnswer = i;
            }
            ioService.printFormattedLine(i + ". " + answer.text());
            i++;
        }
        var answer = ioService.readIntForRangeWithPrompt(1, question.answers().size(),
                ioService.getMessage("Answer.input"),
                ioService.getMessage("Answer.range", question.answers().size()));
        isAnswerValid = answer == rightAnswer;
        testResult.applyAnswer(question, isAnswerValid);
        if (isAnswerValid) {
            ioService.printTagFormattedLine(Tag.ANSWER_CORRECT, ioService.getMessage("Answer.correct"));
        } else {
            ioService.printTagFormattedLine(Tag.ANSWER_WRONG, ioService.getMessage("Answer.wrong"));
        }
        ioService.printLine("");
    }

}
