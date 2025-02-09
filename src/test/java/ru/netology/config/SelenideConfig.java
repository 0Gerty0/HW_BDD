package ru.netology.config;

import com.codeborne.selenide.Configuration;

public class SelenideConfig {
    static {
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.timeout = 5000;
    }
}

