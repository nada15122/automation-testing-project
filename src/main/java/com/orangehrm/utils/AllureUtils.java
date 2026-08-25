package com.orangehrm.utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class AllureUtils {

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver, String name) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}