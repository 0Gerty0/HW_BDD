package ru.netology;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.config.SelenideConfig;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest extends BaseTest {
    private DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        Selenide.open("http://localhost:9999"); // Открываем страницу логина
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        var verificationCode = DataHelper.getVerificationCode().getCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();
        int amount = 500;

        // Извлекаем только последние 4 цифры карт
        String firstCardLast4 = firstCard.getCardNumber().substring(firstCard.getCardNumber().length() - 4);
        String secondCardLast4 = secondCard.getCardNumber().substring(secondCard.getCardNumber().length() - 4);

        // Получаем балансы по последним 4 цифрам
        int initialBalanceFirstCard = dashboardPage.getBalance(firstCardLast4);
        int initialBalanceSecondCard = dashboardPage.getBalance(secondCardLast4);

        // Переводим деньги с первой карты на вторую
        var transferPage = dashboardPage.transferToCard(secondCardLast4);
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), firstCardLast4);

        // Проверяем, что баланс изменился корректно
        assertEquals(initialBalanceFirstCard - amount, dashboardPage.getBalance(firstCardLast4));
        assertEquals(initialBalanceSecondCard + amount, dashboardPage.getBalance(secondCardLast4));
    }
}

