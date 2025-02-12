package ru.netology.config;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;

public class SelenideConfig {
    static {
        System.out.println("SelenideConfig static block executing");
        // Явно задаем браузер и включаем headless‑режим
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 20000;
        System.setProperty("file.encoding", "UTF-8");

        ChromeOptions options = new ChromeOptions();
        // Задаем headless через метод setHeadless
        options.setHeadless(true);
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--lang=ru");
        options.addArguments("--disable-gpu");
        Configuration.browserCapabilities = options;

        System.out.println("Headless mode set: " + Configuration.headless);
    }
}

