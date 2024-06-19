package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.formatters.AnswerFormatter;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final AnswerFormatter answerFormatter;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        List<Question> questionList = questionDao.findAll();
        ioService.printFormattedLine(createQuestionString(questionList).toString());
        ioService.printLine("");
    }

    private StringBuilder createQuestionString(List<Question> questionList) {
        StringBuilder questionSb = new StringBuilder();
        for (Question qst : questionList) {
            questionSb.append("Question: ").append(qst.text()).append(" %n");
            questionSb.append(qst.answers().stream()
                    .map(answerFormatter::format)
                    .collect(Collectors.joining("%n")));
            questionSb.append(" %n");
        }
        return questionSb;
    }

}
