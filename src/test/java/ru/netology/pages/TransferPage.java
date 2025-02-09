package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountInput = $("[data-test-id='amount'] input");
    private SelenideElement fromInput = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");

    public DashboardPage makeTransfer(String amount, String fullCardNumber) {
        amountInput.setValue(amount);
        fromInput.setValue(fullCardNumber);
        transferButton.click();
        return new DashboardPage();
    }
}

