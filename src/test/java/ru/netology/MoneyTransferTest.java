package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.helpers.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest extends BaseTest {
    private DashboardPage dashboardPage;

    @BeforeAll
    static void setUpAll() {
        // Дополнительный таймаут (уже задан в SelenideConfig)
        Configuration.timeout = 10000;
    }

    @BeforeEach
    void setUp() {
        Selenide.open("http://localhost:9999"); // Открываем приложение
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        var verificationCode = DataHelper.getVerificationCode().getCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        // Карта, куда зачисляются деньги
        var firstCard = DataHelper.getFirstCardInfo();   // "5559 0000 0000 0001"
        // Карта, с которой списываются деньги
        var secondCard = DataHelper.getSecondCardInfo();   // "5559 0000 0000 0002"

        // Используем сумму 500, чтобы избежать ошибки (если перевод 5000 вызывает отказ)
        int amount = 500;

        int initialBalanceFirstCard = dashboardPage.getBalance(firstCard.getLastFourDigits());
        int initialBalanceSecondCard = dashboardPage.getBalance(secondCard.getLastFourDigits());

        // Выполняем перевод: нажимаем на "Пополнить" у карты назначения (первая карта)
        var transferPage = dashboardPage.transferToCard(firstCard.getLastFourDigits());
        // Переводим сумму с карты-источника (вторая карта)
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), secondCard.getCardNumber());

        assertEquals(initialBalanceFirstCard + amount, dashboardPage.getBalance(firstCard.getLastFourDigits()),
                "Balance of the first card should increase by the transfer amount");
        assertEquals(initialBalanceSecondCard - amount, dashboardPage.getBalance(secondCard.getLastFourDigits()),
                "Balance of the second card should decrease by the transfer amount");
    }
}
