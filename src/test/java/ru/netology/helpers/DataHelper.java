package ru.netology.helpers;

import lombok.Value;

public class DataHelper {

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("5559 0000 0000 0001");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("5559 0000 0000 0002");
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    @Value
    public static class CardInfo {
        String fullCardNumber;

        public String getLastFourDigits() {
            return fullCardNumber.substring(fullCardNumber.length() - 4);
        }

        public String getCardNumber() { // Returns the full card number
            return fullCardNumber;
        }
    }
}
