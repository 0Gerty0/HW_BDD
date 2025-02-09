package ru.netology;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest extends BaseTest {
    private DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        Selenide.open("http://localhost:9999");
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

        int initialBalanceFirstCard = dashboardPage.getBalance(firstCard.getLastFourDigits());
        int initialBalanceSecondCard = dashboardPage.getBalance(secondCard.getLastFourDigits());

        var transferPage = dashboardPage.transferToCard(secondCard.getLastFourDigits());
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), firstCard.getFullCardNumber());

        assertEquals(initialBalanceFirstCard - amount, dashboardPage.getBalance(firstCard.getLastFourDigits()));
        assertEquals(initialBalanceSecondCard + amount, dashboardPage.getBalance(secondCard.getLastFourDigits()));
    }
}
