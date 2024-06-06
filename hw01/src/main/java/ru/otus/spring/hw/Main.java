package ru.otus.spring.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.hw.service.QuestionServiceImpl;

public class Main {

    public static void main(String[] args) {
        // создайте здесь класс контекста
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        // Получите Service
        var questionService = context.getBean(QuestionServiceImpl.class);
        questionService.printAll();
    }
}
