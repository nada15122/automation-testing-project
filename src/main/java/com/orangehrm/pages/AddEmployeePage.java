package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import com.orangehrm.utils.AllureUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AddEmployeePage extends BasePage {

    private final By firstNameInput = By.name("firstName");
    private final By lastNameInput = By.name("lastName");
    private final By employeeIdInput = By.xpath("//label[text()='Employee Id']/parent::div/following-sibling::div/input");
    private final By saveButton = By.cssSelector("button[type='submit']");
    private final By formLoader = By.className("oxd-form-loader");
    private final By firstNameError =
            By.xpath("//input[@name='firstName']/ancestor::div[contains(@class,'oxd-input-group')]//span[text()='Required']");
    private final By personalDetailsHeader = By.xpath("//h6[normalize-space()='Personal Details']");
    private final By employeeFullNameHeader = By.cssSelector(".orangehrm-edit-employee-name h6");

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    public boolean isFirstNameDisplayed() {
        return isDisplayed(firstNameInput);
    }

    public boolean isLastNameDisplayed() {
        return isDisplayed(lastNameInput);
    }

    @Step("Enter first name: {0}")
    public AddEmployeePage enterFirstName(String firstName) {
        type(firstNameInput, firstName);
        AllureUtils.takeScreenshot(driver, "Entered First Name");
        return this;
    }

    @Step("Enter last name: {0}")
    public AddEmployeePage enterLastName(String lastName) {
        type(lastNameInput, lastName);
        AllureUtils.takeScreenshot(driver, "Entered Last Name");
        return this;
    }

    @Step("Enter custom unique employee ID: {0}")
    public AddEmployeePage enterEmployeeId(String empId) {
        visible(employeeIdInput);
        driver.findElement(employeeIdInput).sendKeys(Keys.CONTROL + "a");
        driver.findElement(employeeIdInput).sendKeys(Keys.BACK_SPACE);
        driver.findElement(employeeIdInput).sendKeys(empId);
        AllureUtils.takeScreenshot(driver, "Entered Employee ID");
        return this;
    }

    @Step("Click Save")
    public AddEmployeePage clickSave() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        AllureUtils.takeScreenshot(driver, "Before Clicking Save");
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        return this;
    }

    public boolean isFirstNameRequiredErrorDisplayed() {
        return isDisplayed(firstNameError);
    }

    public boolean isPersonalDetailsPageDisplayed() {
        waitForUrlContains("/pim/viewPersonalDetails");
        return isDisplayed(personalDetailsHeader);
    }

    @Step("Read the created employee name from the header")
    public String getEmployeeNameHeader() {
        waitForUrlContains("/pim/viewPersonalDetails");
        visible(employeeFullNameHeader);
        wait.until(ExpectedConditions.attributeToBeNotEmpty(
                driver.findElement(employeeFullNameHeader), "innerText"
        ));
        AllureUtils.takeScreenshot(driver, "Employee Personal Details Header");
        return driver.findElement(employeeFullNameHeader).getText().trim();
    }
}