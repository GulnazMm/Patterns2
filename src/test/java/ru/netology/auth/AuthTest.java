package ru.netology.auth;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static ru.netology.auth.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.auth.DataGenerator.Registration.getUser;
import static ru.netology.auth.DataGenerator.getRandomLogin;
import static ru.netology.auth.DataGenerator.getRandomPassword;


class AuthTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Successfully login  with active user registered")
    void successfulLoginIfActiveUserRegistered() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("#root").shouldHave(exactText("Личный кабинет"));
    }

    @Test
    @DisplayName("Error message if logged in with a blocked registered user")
    void getErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldHave(exactText("Ошибка Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("If login with not registered user")
    void getErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input ").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Error message if login with incorrect login")
    void getErrorIfIncorrectLogin() {
        var registeredUser = getRegisteredUser("active");
        var incorrectLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(incorrectLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Error message if login with incorrect password")
    void getErrorIfIncorrectPassword() {
        var registeredUser = getRegisteredUser("active");
        var incorrectPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(incorrectPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void getErrorBlockedUserIncorrectLogin() {
        var blockedUser = getRegisteredUser("blocked");
        var incorrectLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(incorrectLogin);
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void getErrorBlockedUserIncorrectPassword() {
        var blockedUser = getRegisteredUser("blocked");
        var incorrectPassword = getRandomLogin();
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(incorrectPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }
}



