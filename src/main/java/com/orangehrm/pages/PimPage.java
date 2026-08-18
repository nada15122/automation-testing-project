package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
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
        return this;
    }

    @Step("Navigate to PIM > Add Employee")
    public AddEmployeePage openAddEmployee() {
        click(pimSidebarLink);
        click(addEmployeeTab);
        return new AddEmployeePage(driver);
    }

    @Step("Search for employee by name: {0}")
    public PimPage searchEmployee(String employeeName) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(employeeNameInput));

        // 1. مسح الحقل بالكامل
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.BACK_SPACE);

        // 2. إدخال الاسم
        String firstName = employeeName.split(" ")[0];
        input.sendKeys(firstName);

        // 3. اختيار من قائمة الأوتوكومبليت لو ظهرت خلال ثانيتين
        try {
            wait.withTimeout(Duration.ofSeconds(2))
                    .until(ExpectedConditions.visibilityOfElementLocated(autocompleteOption));
            click(autocompleteOption);
        } catch (Exception ignored) {
            // الموظف غير موجود أو الأوتوكومبليت لم يظهر -> كمل سيرش عادي
        }

        // 4. الضغط على زر البحث
        click(searchButton);

        // 5. انتظار اختفاء اللودر لتحديث نتائج الجدول
        try {
            wait.withTimeout(Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(loader));
        } catch (Exception ignored) {
        }

        return this;
    }

    @Step("Check if 'No Records Found' message is displayed")
    public boolean isNoRecordsFoundDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(noRecordsMessage)).isDisplayed();
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
            return driver.findElement(resultsTable).getText();
        } catch (Exception e) {
            return "";
        }
    }
}