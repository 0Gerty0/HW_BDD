package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;

public class DashboardPage {

    // Собираем список карточек
    private ElementsCollection cards = $$("ul.list li")
            .shouldHave(sizeGreaterThan(0), Duration.ofSeconds(10));

    /**
     * Извлекаем баланс карты, у которой в тексте есть указанные последние 4 цифры.
     * Ожидаем текст вида "**** **** **** 0001, баланс: 10500 р."
     */
    public int getBalance(String lastFourDigits) {
        SelenideElement cardElement = cards.findBy(text(lastFourDigits))
                .shouldBe(visible, Duration.ofSeconds(10));
        System.out.println("Found card with last digits: " + lastFourDigits);
        String cardText = cardElement.getText();
        Pattern pattern = Pattern.compile("баланс:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(cardText);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new IllegalStateException("Unable to find balance in text: " + cardText);
        }
    }

    /**
     * Переходим к экрану пополнения карты, у которой в тексте есть указанные последние 4 цифры.
     * На странице ищется кнопка с data-test-id='action-deposit'.
     */
    public TransferPage transferToCard(String lastFourDigits) {
        // Ищем нужную карточку по последним 4 цифрам (например, "0002")
        SelenideElement cardElement = cards.findBy(text(lastFourDigits))
                .shouldBe(visible, Duration.ofSeconds(10));
        System.out.println("Located card: " + lastFourDigits);

        // Находим кнопку "Пополнить" внутри этой карточки
        SelenideElement depositButton = cardElement.$("button[data-test-id='action-deposit']")
                .shouldBe(visible, Duration.ofSeconds(10));
        System.out.println("Deposit button found, scrolling into view...");

        depositButton.scrollIntoView(true);
        // Выполняем клик через JavaScript, чтобы избежать возможных проблем с обычным кликом
        executeJavaScript("arguments[0].click();", depositButton);
        System.out.println("Deposit button clicked, waiting for transfer page...");

        // Добавляем небольшую задержку для перехода (можно корректировать по необходимости)
        sleep(2000);

        // Ожидаем заголовок "Пополнение карты"
        $("h1").shouldHave(text("Пополнение карты"), Duration.ofSeconds(10));
        System.out.println("Navigated to transfer page.");
        return page(TransferPage.class);
    }
}
