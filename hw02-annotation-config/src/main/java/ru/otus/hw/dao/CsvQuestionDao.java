package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<QuestionDto> quest = getQuestionsFromCsv(fileNameProvider.getTestFileName());
        List<Question> allQuestList = new ArrayList<>();
        for (QuestionDto qst : quest) {
            if (!isTheQuestionFormattedCorrectly(qst)) {
                throw new QuestionReadException("Not correct question or too little answers");
            }
            allQuestList.add(qst.toDomainObject());
        }
        return allQuestList;
    }

    private List<QuestionDto> getQuestionsFromCsv(String filename) throws QuestionReadException {
        try (InputStream inputStream = getFileFromResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new QuestionReadException(String.format("Can't read question's file or file %s not found",
                    fileNameProvider.getTestFileName()), e);
        }
    }

    private boolean isTheQuestionFormattedCorrectly(QuestionDto qDto) {
        return ((qDto.getText().length() > 3) && (qDto.getAnswers().size() > 2));
    }

    private InputStream getFileFromResourceAsStream(String filename) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);
        if (inputStream == null) {
            throw new FileNotFoundException("File %s not found");
        } else {
            return inputStream;
        }
    }

}
