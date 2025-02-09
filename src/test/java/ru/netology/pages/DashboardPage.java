package ru.netology.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private ElementsCollection cards = $$("ul.list li.list__item");

    public int getBalance(String lastFourDigits) {
        SelenideElement cardElement = cards.find(text(lastFourDigits));
        String balanceText = cardElement.getText()
                .split("баланс:")[1]
                .trim()
                .replaceAll("[^0-9]", ""); // Убираем все кроме цифр
        return Integer.parseInt(balanceText);
    }

    public TransferPage transferToCard(String lastFourDigits) {
        SelenideElement cardElement = cards.find(text(lastFourDigits));
        cardElement.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }
}

