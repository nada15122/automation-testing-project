package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class LoginPage extends BasePage {

    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By errorAlert = By.cssSelector(".oxd-alert-content-text");
    private final By requiredMessages = By.xpath("//span[text()='Required']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Enter username: {0}")
    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    @Step("Click the Login button")
    public LoginPage clickLogin() {
        click(loginButton);
        return this;
    }

    @Step("Login with username: {0}")
    public DashboardPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new DashboardPage(driver);
    }

    @Step("Read the login error message")
    public String getErrorMessage() {
        return visible(errorAlert).getText().trim();
    }

    @Step("Collect the 'Required' validation messages")
    public List<String> getRequiredValidationMessages() {
        return allVisible(requiredMessages).stream().map(e -> e.getText().trim()).toList();
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }
}
