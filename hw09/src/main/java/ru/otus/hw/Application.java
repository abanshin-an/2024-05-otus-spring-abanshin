package ru.otus.hw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
@Slf4j
public class Application {

    public static final String LOCALHOST_8080 = "http://localhost:8080/";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        openHomePage();
    }

    private static void openHomePage() {
        try {
            System.setProperty("java.awt.headless", "false");
            URI homepage = new URI(LOCALHOST_8080);
            Desktop.getDesktop().browse(homepage);
        } catch (URISyntaxException | IOException e) {
            log.info("open home page at {}", LOCALHOST_8080);
            log.error(" error %s", e);
        }
    }

}
