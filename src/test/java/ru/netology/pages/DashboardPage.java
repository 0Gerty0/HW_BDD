package ru.netology.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private ElementsCollection cards = $$("div[data-test-id]").shouldBe(CollectionCondition.sizeGreaterThan(0), Duration.ofSeconds(5));
    private SelenideElement heading = $("[class*='heading']");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getBalance(String cardLastDigits) {
        SelenideElement cardElement = cards.findBy(text("**** **** **** " + cardLastDigits))
                .shouldBe(visible, Duration.ofSeconds(5));
        String balanceText = cardElement.getText();
        return extractBalance(balanceText);
    }

    private int extractBalance(String text) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalStateException("Не удалось извлечь баланс из текста: " + text);
    }

    public TransferPage transferToCard(String cardLastDigits) {
        SelenideElement cardElement = cards.findBy(text("**** **** **** " + cardLastDigits))
                .shouldBe(visible, Duration.ofSeconds(5));
        cardElement.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }
}
