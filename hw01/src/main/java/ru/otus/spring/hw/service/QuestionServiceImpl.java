package ru.otus.spring.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Answer;
import ru.otus.spring.hw.domain.Question;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final PrintService printService;

    private final QuestionDao questionDao;

    @Override
    public void printAll() {
        List<Question> questionList = questionDao.findAll();
        printService.printFormattedLine("Please answer the questions below");
        printService.printFormattedLine(createQuestionString(questionList).toString());
        printService.printLine("");
    }

    private StringBuilder createQuestionString(List<Question> questionList) {
        StringBuilder questionSb = new StringBuilder();
        for (Question qst : questionList) {
            questionSb.append("Question: ").append(qst.getQuestionText()).append(" %n");
            questionSb.append(qst.getAnswerList().stream().map(Answer::toString).collect(Collectors.joining("%n")));
            questionSb.append(" %n");
        }
        return questionSb;
    }
}
