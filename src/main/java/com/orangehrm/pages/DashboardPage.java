package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class DashboardPage extends BasePage {

    private final By dashboardHeader = By.xpath("//h6[normalize-space()='Dashboard']");
    private final By sidebarItems = By.cssSelector(".oxd-main-menu-item--name");
    private final By topbarItems = By.cssSelector(".oxd-topbar-body-nav-tab-item");
    private final By footerText = By.cssSelector(".oxd-layout-footer .orangehrm-copyright, .oxd-layout-footer");
    private final By footerLink = By.xpath("//div[contains(@class,'oxd-layout-footer')]//a[contains(.,'OrangeHRM')]");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Wait for the dashboard to load")
    public DashboardPage waitUntilLoaded() {
        waitForUrlContains("/dashboard/index");
        visible(dashboardHeader);
        return this;
    }

    public boolean isDashboardHeaderDisplayed() {
        return isDisplayed(dashboardHeader);
    }

    @Step("Read the sidebar module names")
    public List<String> getSidebarModules() {
        return allVisible(sidebarItems).stream().map(e -> e.getText().trim()).toList();
    }

    @Step("Open the {0} module from the sidebar")
    public void openModule(String moduleName) {
        click(By.xpath("//span[normalize-space()='" + moduleName + "']/ancestor::a"));
    }

    @Step("Read the footer text")
    public String getFooterText() {
        scrollToBottom();
        return visible(footerText).getText().trim();
    }

    @Step("Click the OrangeHRM, Inc footer link")
    public void clickFooterLink() {
        scrollToBottom();
        click(footerLink);
    }
}
