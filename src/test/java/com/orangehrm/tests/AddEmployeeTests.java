package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.AddEmployeePage;
import com.orangehrm.pages.PimPage;
import com.orangehrm.utils.JsonDataReader;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Feature("PIM - Add Employee")
public class AddEmployeeTests extends BaseTest {

    @DataProvider(name = "newEmployee")
    public Object[][] newEmployee() {
        String unique = String.valueOf(System.currentTimeMillis()).substring(7);
        return new Object[][]{{
                JsonDataReader.text("newEmployee", "firstName"),
                JsonDataReader.text("newEmployee", "lastName") + unique
        }};
    }

    @Test(description = "TC06 - Open the Add Employee page")
    @Story("Add Employee page loads")
    public void testOpenAddEmployeePage() {
        log.info("TC06 - opening the Add Employee page");
        loginWithValidUser();
        AddEmployeePage page = new PimPage(driver).openAddEmployee();

        assertTrue(page.currentUrl().contains("/pim/addEmployee"),
                "URL should contain /pim/addEmployee but was: " + page.currentUrl());
        assertTrue(page.isFirstNameDisplayed(), "First Name field should be displayed");
        assertTrue(page.isLastNameDisplayed(), "Last Name field should be displayed");
    }

    @Test(description = "TC07 - Add employee with an empty required field")
    @Story("Required field validation")
    public void testAddEmployeeWithEmptyFirstName() {
        log.info("TC07 - saving an employee without a first name");
        loginWithValidUser();
        AddEmployeePage page = new PimPage(driver).openAddEmployee();
        page.enterLastName("OnlyLastName").clickSave();

        assertTrue(page.isFirstNameRequiredErrorDisplayed(),
                "'Required' validation should appear under First Name");
    }

    @Test(dataProvider = "newEmployee", description = "TC08 - End-to-end: add a new employee successfully")
    @Story("End-to-end employee creation")
    public void testAddEmployeeEndToEnd(String firstName, String lastName) {
        log.info("TC08 - creating employee {} {}", firstName, lastName);
        loginWithValidUser();
        AddEmployeePage page = new PimPage(driver).openAddEmployee();

        String uniqueEmpId = String.valueOf(System.currentTimeMillis()).substring(7);

        page.enterFirstName(firstName)
                .enterLastName(lastName)
                .enterEmployeeId(uniqueEmpId)
                .clickSave();

        assertTrue(page.isPersonalDetailsPageDisplayed(),
                "Personal Details page should open for the new employee");
        assertTrue(page.getEmployeeNameHeader().contains(firstName),
                "Employee name header should contain " + firstName);

        PimPage pim = new PimPage(driver).openEmployeeList().searchEmployee(firstName);

        assertTrue(pim.getResultsText().contains(lastName),
                "The new employee should appear in the Employee List results");
    }
}