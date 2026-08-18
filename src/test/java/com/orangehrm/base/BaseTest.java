package com.orangehrm.base;

import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.DriverFactory;
import com.orangehrm.utils.JsonDataReader;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.createDriver();
        driver = DriverFactory.getDriver();
        log.info("Driver started for thread {}", Thread.currentThread().getId());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            DriverFactory.quitDriver();
        }
        log.info("Closing driver for thread {}", Thread.currentThread().getId());
    }

    @Step("Capture Page Screenshot")
    @Attachment(value = "Page Screenshot", type = "image/png")
    public byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    protected LoginPage openLoginPage() {
        driver.get(ConfigReader.baseUrl() + "auth/login");
        return new LoginPage(driver);
    }

    protected void loginWithValidUser() {
        String username = JsonDataReader.text("validLogin", "username");
        String password = JsonDataReader.text("validLogin", "password");
        openLoginPage().login(username, password);
        log.info("Logged in as {}", username);
    }
}