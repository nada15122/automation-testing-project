package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.AdminPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@Feature("Admin - User Management")
public class AdminTests extends BaseTest {

    @Test(description = "TC09 - Verify the Admin > Add User page fields")
    @Story("Add User form")
    public void testAddUserFormFields() {
        log.info("TC09 - verifying the Add User form fields");
        loginWithValidUser();
        AdminPage admin = new AdminPage(driver).openUsers().clickAdd();

        assertTrue(admin.isUserRoleFieldDisplayed(), "User Role field should be displayed");
        assertTrue(admin.isEmployeeNameFieldDisplayed(), "Employee Name field should be displayed");
        assertTrue(admin.isUsernameFieldDisplayed(), "Username field should be displayed");
        assertTrue(admin.isPasswordFieldDisplayed(), "Password field should be displayed");
    }
}
