package ru.otus.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "io")
public class IoProperties implements IoConfig {

    private int maxAnswerAttempts;

}
