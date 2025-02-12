package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import ru.netology.config.SelenideConfig; // Ensure SelenideConfig settings are loaded

public class BaseTest {
    @BeforeAll
    static void setup() {
        // Force initialization of SelenideConfig
        Class<?> dummy = SelenideConfig.class;
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.browserSize = "1920x1080";
    }
}
