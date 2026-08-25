package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import com.orangehrm.utils.AllureUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class PimPage extends BasePage {

    private final By pimSidebarLink = By.xpath("//span[normalize-space()='PIM']/ancestor::a");
    private final By employeeListTab = By.xpath("//a[normalize-space()='Employee List']");
    private final By addEmployeeTab = By.xpath("//a[normalize-space()='Add Employee']");
    private final By employeeNameInput =
            By.xpath("//label[normalize-space()='Employee Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By searchButton = By.cssSelector("button[type='submit']");
    private final By resultsTable = By.cssSelector(".oxd-table-body");
    private final By tableRows = By.cssSelector(".oxd-table-card");
    private final By noRecordsMessage = By.xpath("//span[contains(.,'No Records Found')]");
    private final By loader = By.cssSelector(".oxd-loading-spinner");
    private final By autocompleteOption = By.cssSelector(".oxd-autocomplete-option");

    public PimPage(WebDriver driver) {
        super(driver);
    }

    @Step("Navigate to PIM > Employee List")
    public PimPage openEmployeeList() {
        click(pimSidebarLink);
        click(employeeListTab);
        visible(employeeNameInput);
        AllureUtils.takeScreenshot(driver, "PIM Employee List Page");
        return this;
    }

    @Step("Navigate to PIM > Add Employee")
    public AddEmployeePage openAddEmployee() {
        click(pimSidebarLink);
        click(addEmployeeTab);
        AllureUtils.takeScreenshot(driver, "Navigated to Add Employee");
        return new AddEmployeePage(driver);
    }

    @Step("Search for employee by name: {0}")
    public PimPage searchEmployee(String employeeName) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(employeeNameInput));

        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.BACK_SPACE);

        String firstName = employeeName.split(" ")[0];
        input.sendKeys(firstName);

        try {
            wait.withTimeout(Duration.ofSeconds(1)) // تقليل الانتظار لثانية واحدة
                    .until(ExpectedConditions.visibilityOfElementLocated(autocompleteOption));
            click(autocompleteOption);
        } catch (Exception ignored) {
        }

        click(searchButton);

        try {
            wait.withTimeout(Duration.ofSeconds(3)) // تقليل انتظار اختفاء الـ loader لـ 3 ثوانٍ فقط
                    .until(ExpectedConditions.invisibilityOfElementLocated(loader));
        } catch (Exception ignored) {
        }

        AllureUtils.takeScreenshot(driver, "Search Results List");
        return this;
    }

    @Step("Check if 'No Records Found' message is displayed")
    public boolean isNoRecordsFoundDisplayed() {
        try {
            boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(noRecordsMessage)).isDisplayed();
            if (isDisplayed) {
                AllureUtils.takeScreenshot(driver, "No Records Found State");
            }
            return isDisplayed;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get results table text")
    public String getResultsText() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(tableRows),
                    ExpectedConditions.visibilityOfElementLocated(noRecordsMessage)
            ));

            if (isNoRecordsFoundDisplayed()) {
                return "No Records Found";
            }
            String results = driver.findElement(resultsTable).getText();
            AllureUtils.takeScreenshot(driver, "Employee Table Results");
            return results;
        } catch (Exception e) {
            return "";
        }
    }
}