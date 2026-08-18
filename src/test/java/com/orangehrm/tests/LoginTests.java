package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.JsonDataReader;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

@Feature("Authentication")
public class LoginTests extends BaseTest {

    @DataProvider(name = "validCredentials")
    public Object[][] validCredentials() {
        return new Object[][]{{
                JsonDataReader.text("validLogin", "username"),
                JsonDataReader.text("validLogin", "password")
        }};
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return JsonDataReader.credentialsAsDataProvider("invalidLogins");
    }

    @Test(dataProvider = "validCredentials", description = "TC01 - Login with valid credentials")
    @Story("Valid login")
    @Description("Logs in with valid credentials and verifies the dashboard is reached.")
    public void testLoginWithValidCredentials(String username, String password) {
        log.info("TC01 - login with valid credentials: {}", username);
        DashboardPage dashboard = openLoginPage().login(username, password).waitUntilLoaded();

        assertTrue(dashboard.currentUrl().contains("/dashboard/index"),
                "URL should contain /dashboard/index but was: " + dashboard.currentUrl());
        assertTrue(dashboard.isDashboardHeaderDisplayed(), "Dashboard header should be displayed");
    }

    @Test(dataProvider = "invalidCredentials", description = "TC02 - Login with invalid credentials")
    @Story("Invalid login")
    public void testLoginWithInvalidCredentials(String username, String password) {
        log.info("TC02 - login with invalid credentials: {}", username);
        LoginPage loginPage = openLoginPage();
        loginPage.enterUsername(username).enterPassword(password).clickLogin();

        assertEquals(loginPage.getErrorMessage(), "Invalid credentials",
                "Expected the 'Invalid credentials' error message");
    }

    @Test(description = "TC03 - Login with empty fields")
    @Story("Empty field validation")
    public void testLoginWithEmptyFields() {
        log.info("TC03 - login with empty fields");
        LoginPage loginPage = openLoginPage();
        loginPage.clickLogin();

        List<String> messages = loginPage.getRequiredValidationMessages();
        assertEquals(messages.size(), 2,
                "Two 'Required' messages expected (username + password), found: " + messages);
        assertTrue(messages.stream().allMatch(m -> m.equals("Required")),
                "All validation messages should read 'Required': " + messages);
    }
}
