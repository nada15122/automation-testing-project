package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.utils.JsonDataReader;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

@Feature("UI & Branding")
public class UiTests extends BaseTest {

    @Test(description = "TC10 - Verify the OrangeHRM footer branding link")
    @Story("Footer branding")
    public void testFooterBrandingLink() {
        log.info("TC10 - verifying the footer branding link");
        loginWithValidUser();
        DashboardPage dashboard = new DashboardPage(driver).waitUntilLoaded();

        assertTrue(dashboard.getFooterText().contains("OrangeHRM, Inc"),
                "Footer text should contain 'OrangeHRM, Inc'");

        String originalTab = driver.getWindowHandle();
        dashboard.clickFooterLink();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.numberOfWindowsToBe(2));
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        handles.remove(originalTab);
        driver.switchTo().window(handles.get(0));

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("orangehrm.com"));
        assertTrue(driver.getCurrentUrl().contains("orangehrm.com"),
                "New tab URL should contain orangehrm.com but was: " + driver.getCurrentUrl());

        driver.close();
        driver.switchTo().window(originalTab);
    }

    @Test(description = "TC11 - Verify the sidebar menu items")
    @Story("Sidebar menu")
    public void testSidebarMenuItems() {
        log.info("TC11 - verifying the sidebar menu items");
        loginWithValidUser();
        DashboardPage dashboard = new DashboardPage(driver).waitUntilLoaded();

        List<String> actual = dashboard.getSidebarModules();
        JsonNode expected = JsonDataReader.node("sidebarModules");
        for (JsonNode module : expected) {
            assertTrue(actual.contains(module.asText()),
                    "Sidebar should contain '" + module.asText() + "' but items were: " + actual);
        }
    }
}
