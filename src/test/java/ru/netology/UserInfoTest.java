package ru.netology;

import com.codeborne.selenide.Condition;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.*;

public class UserInfoTest {
    SelenideElement form;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        form = $(".form");
    }

    private void loginPage(String login, String password) {
        $("[data-test-id='login'] input").setValue(login);
        $("[data-test-id='password'] input").setValue(password);
        $("[data-test-id='action-login']").click();
    }

    @Test
    public void shouldGetSuccessIfUserIsRegisteredAndActive() {
        UserInfo user = generateValidUser("en", false);
        loginPage(user.getLogin(), user.getPassword());
        $("h2").waitUntil(Condition.visible, 15000).shouldHave(text("Личный кабинет"));
    }

    @Test
    public void shouldGetErrorIfUserIsBlocked() {
        UserInfo user = generateValidUser("en", true);
        loginPage(user.getLogin(), user.getPassword());
        $("[data-test-id='error-notification']").waitUntil(Condition.visible, 15000).shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    public void shouldGetErrorIfUserIsNotRegistered() {
        UserInfo user = generateUser("en", false);
        loginPage(user.getLogin(), user.getPassword());
        $("[data-test-id='error-notification']").waitUntil(Condition.visible, 15000).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldGetErrorIfLoginIsInvalid() {
        UserInfo user = generateInvalidLogin("en", false);
        loginPage(user.getLogin(), user.getPassword());
        $("[data-test-id='error-notification']").waitUntil(Condition.visible, 15000).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldGetErrorIfPasswordIsInvalid() {
        UserInfo user = generateInvalidPassword("en", false);
        loginPage(user.getLogin(), user.getPassword());
        $("[data-test-id='error-notification']").waitUntil(Condition.visible, 15000).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}
