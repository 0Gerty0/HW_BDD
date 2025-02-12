package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TransferPage {
    private SelenideElement amountField = $("[data-test-id=amount] input")
            .shouldBe(visible, Duration.ofSeconds(10));
    private SelenideElement fromField = $("[data-test-id=from] input")
            .shouldBe(visible, Duration.ofSeconds(10));
    private SelenideElement transferButton = $("[data-test-id=action-transfer]")
            .shouldBe(visible, Duration.ofSeconds(10));

    public TransferPage() {
        System.out.println("Transfer page opened, fields are visible.");
    }

    // Метод, имитирующий медленный ввод символов
    private void typeSlowly(SelenideElement element, String text) {
        element.click();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            sleep(100); // задержка 100 мс между символами
        }
    }

    public DashboardPage makeTransfer(String amount, String fromCard) {
        // Ввод суммы с медленным набором
        System.out.println("Typing amount slowly: " + amount);
        typeSlowly(amountField, amount);
        // Проверка значения, игнорируя пробелы (если, например, форматируется как "5 000")
        amountField.shouldHave(new Condition("value ignoring spaces") {
            @Override
            public boolean apply(Driver driver, WebElement element) {
                String actual = element.getAttribute("value").replace(" ", "");
                return actual.equals(amount);
            }
            public String actualValue(WebElement element) {
                return element.getAttribute("value");
            }
        }, Duration.ofSeconds(5));
        Selenide.screenshot("after_filling_amount");

        // Ввод номера карты отправителя с нормализацией (удаляем пробелы)
        System.out.println("Typing sender card number slowly...");
        String normalizedFromCard = fromCard.replace(" ", "");
        typeSlowly(fromField, normalizedFromCard);
        fromField.shouldHave(new Condition("value ignoring spaces") {
            @Override
            public boolean apply(Driver driver, WebElement element) {
                String actual = element.getAttribute("value").replace(" ", "");
                return actual.equals(normalizedFromCard);
            }
            public String actualValue(WebElement element) {
                return element.getAttribute("value");
            }
        }, Duration.ofSeconds(5));
        Selenide.screenshot("after_filling_card");

        // Нажатие на кнопку перевода
        System.out.println("Clicking transfer button...");
        transferButton.click();
        Selenide.screenshot("after_clicking_transfer");

        // Если появляется уведомление об ошибке, выбрасываем исключение
        if ($("[data-test-id='error-notification']").is(visible)) {
            String errorText = $("[data-test-id='error-notification']").getText();
            System.out.println("Error notification: " + errorText);
            throw new IllegalStateException("Transfer failed due to error: " + errorText);
        }

        // Ожидаем переход на дашборд с заголовком "Ваши карты"
        $("h1").shouldHave(text("Ваши карты"), Duration.ofSeconds(15));
        System.out.println("Transfer completed, returning to dashboard.");

        return page(DashboardPage.class);
    }
}
