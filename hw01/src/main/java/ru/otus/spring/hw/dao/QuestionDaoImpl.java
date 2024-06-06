package ru.otus.spring.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.config.QuestionsFileNameProvider;
import ru.otus.spring.hw.domain.Question;
import ru.otus.spring.hw.dto.QuestionDto;
import ru.otus.spring.hw.exceptions.QuestionsLoadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class QuestionDaoImpl implements QuestionDao {
    private final QuestionsFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() throws QuestionsLoadException {
        List<QuestionDto> quest = getQuestionsFromCsv(fileNameProvider.getQuestionsFileName());
        List<Question> allQuestList = new ArrayList<>();
        for (QuestionDto qst : quest) {
            if (!checkCorrectLine(qst)) {
                throw new QuestionsLoadException("Not correct question or too little answers");
            }
            allQuestList.add(qst.toDomainObject());
        }
        return allQuestList;
    }

    private List<QuestionDto> getQuestionsFromCsv(String filename) throws QuestionsLoadException {
        try (InputStream inputStream = getFileFromResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new QuestionsLoadException(String.format("Can't read question's file or file %s not found",
                    fileNameProvider.getQuestionsFileName()), e);
        }
    }

    private boolean checkCorrectLine(QuestionDto qDto) {
        return ((qDto.getText().length() > 3) && (qDto.getAnswers().size() > 2));
    }

    private InputStream getFileFromResourceAsStream(String filename) throws QuestionsLoadException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);
        if (inputStream == null) {
            throw new QuestionsLoadException(String.format("File %s not found", filename), new RuntimeException());
        } else {
            return inputStream;
        }
    }
}
