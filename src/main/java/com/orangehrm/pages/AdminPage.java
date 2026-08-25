package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import com.orangehrm.utils.AllureUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminPage extends BasePage {

    private final By adminSidebarLink = By.xpath("//span[normalize-space()='Admin']/ancestor::a");
    private final By addButton = By.xpath("//button[normalize-space()='Add']");
    private final By addUserHeader = By.xpath("//h6[normalize-space()='Add User']");
    private final By usernameInput = By.xpath("//label[normalize-space()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By passwordInput = By.xpath("//label[normalize-space()='Password']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By employeeNameInput = By.xpath("//label[normalize-space()='Employee Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By userRoleLabel = By.xpath("//label[normalize-space()='User Role']");

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    @Step("Navigate to Admin > User Management > Users")
    public AdminPage openUsers() {
        click(adminSidebarLink);
        waitForUrlContains("/admin/viewSystemUsers");
        AllureUtils.takeScreenshot(driver, "Admin Users Page");
        return this;
    }

    @Step("Click Add to open the Add User form")
    public AdminPage clickAdd() {
        click(addButton);
        visible(addUserHeader);
        AllureUtils.takeScreenshot(driver, "Add User Form");
        return this;
    }

    public boolean isUserRoleFieldDisplayed() {
        return isDisplayed(userRoleLabel);
    }

    public boolean isEmployeeNameFieldDisplayed() {
        return isDisplayed(employeeNameInput);
    }

    public boolean isUsernameFieldDisplayed() {
        return isDisplayed(usernameInput);
    }

    public boolean isPasswordFieldDisplayed() {
        return isDisplayed(passwordInput);
    }
}