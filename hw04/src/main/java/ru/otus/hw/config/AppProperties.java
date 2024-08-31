package ru.otus.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Setter
@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig {

    @Getter
    private int rightAnswersCountToPass;

    @Getter
    private Locale locale;

    private String tryLanguageTag;

    private Map<String, String> fileNameByLocaleTag;

    public void setFileNameByLocaleTag(Map<String, String> fileNameByLocaleTag) {
        this.fileNameByLocaleTag = fileNameByLocaleTag;
        if (fileNameByLocaleTag != null) {
            if (tryLanguageTag != null) {
                setLocale(tryLanguageTag);
                tryLanguageTag = null;
            }
        }
    }

    public void setLocale(String languageTag) {
        if (fileNameByLocaleTag == null) {
            tryLanguageTag = languageTag;
        }
        if (Objects.nonNull(fileNameByLocaleTag) && getEnabledLocales().contains(languageTag)) {
            this.locale = Locale.forLanguageTag(languageTag);
        } else {
            this.locale = Locale.getDefault();
        }
    }

    @Override
    public Set<String> getEnabledLocales() {
        return fileNameByLocaleTag.keySet();
    }

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }

}
