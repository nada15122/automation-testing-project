package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.PimPage;
import com.orangehrm.utils.JsonDataReader;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Epic("OrangeHRM Web Application")
@Feature("Employee Search Functionality")
public class EmployeeSearchTests extends BaseTest {


    @DataProvider(name = "existingEmployees")
    public Object[][] getExistingEmployees() {
        return new Object[][]{
                { JsonDataReader.text("existingEmployee", "employeeName") }
        };
    }

    @DataProvider(name = "nonExistingEmployees")
    public Object[][] getNonExistingEmployees() {
        return new Object[][]{
                { JsonDataReader.text("nonExistingEmployee", "employeeName") }
        };
    }

    @Test(dataProvider = "existingEmployees", description = "TC04 - Search for an existing employee")
    @Story("Search an existing employee")
    public void testSearchExistingEmployee(String employeeName) {
        log.info("TC04 - searching for existing employee: {}", employeeName);
        loginWithValidUser();

        PimPage pim = new PimPage(driver)
                .openEmployeeList()
                .searchEmployee(employeeName);

        String resultsText = pim.getResultsText();

        takeScreenshot();

        String firstName = employeeName.split(" ")[0];
        assertTrue(resultsText.toLowerCase().contains(firstName.toLowerCase()),
                "Results table should contain '" + firstName + "' but got: " + resultsText);
    }

    @Test(dataProvider = "nonExistingEmployees", description = "TC05 - Search for a non-existing employee")
    @Story("Search for non-existing employee")
    public void testSearchNonExistingEmployee(String employeeName) {
        log.info("TC05 - searching for non-existing employee: {}", employeeName);
        loginWithValidUser();

        PimPage pim = new PimPage(driver)
                .openEmployeeList()
                .searchEmployee(employeeName);
        String resultsText = pim.getResultsText();

        takeScreenshot();

        assertTrue(resultsText.contains("No Records Found"),
                "Expected results to contain 'No Records Found' but got: " + resultsText);
    }
}