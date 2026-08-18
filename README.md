# Automation Testing Project — OrangeHRM (Selenium + TestNG)

Automated UI tests for the OrangeHRM open-source demo, built with Selenium WebDriver 4,
TestNG and the Page Object Model.

**Site under test:** https://opensource-demo.orangehrmlive.com/web/index.php/auth/login

## Tech stack
- Java 17, Maven
- Selenium WebDriver 4 (built-in Selenium Manager — no manual driver downloads)
- TestNG (parallel execution, DataProviders, IRetryAnalyzer)
- Jackson (JSON test data)
- Log4j2 (logging to `logs/automation.log`)
- Allure (HTML reporting)

## Project structure
```
src/test/java/com/orangehrm
├── base/        BaseTest (driver lifecycle), BasePage (explicit waits)
├── pages/       LoginPage, DashboardPage, PimPage, AddEmployeePage, AdminPage
├── tests/       LoginTests, EmployeeSearchTests, AddEmployeeTests, AdminTests, UiTests
├── listeners/   RetryAnalyzer, RetryListener
└── utils/       ConfigReader, JsonDataReader, DriverFactory (ThreadLocal<WebDriver>)
src/test/resources
├── config.properties   base.url, browser, explicit.wait, headless
├── testdata.json       users, employee names, sidebar modules
└── log4j2.xml
testng.xml               parallel="tests" thread-count="3"
```

## Design notes
- **POM**: every page exposes actions/queries; tests contain assertions only.
- **No `Thread.sleep()`**: all synchronisation uses `WebDriverWait` with the timeout from `config.properties`.
- **Parallel-safe**: `DriverFactory` stores the driver in a `ThreadLocal<WebDriver>`, so each `<test>` thread gets its own browser.
- **Data-driven**: credentials and employee names come from `testdata.json` via TestNG DataProviders.
- **Retry**: `RetryListener` (IAnnotationTransformer) attaches `RetryAnalyzer` to every test — up to 2 retries.

## Test cases
| # | Test | Class |
|---|------|-------|
| 1 | Login with valid credentials | LoginTests |
| 2 | Login with invalid credentials | LoginTests |
| 3 | Login with empty fields ("Required") | LoginTests |
| 4 | Search an existing employee (PIM) | EmployeeSearchTests |
| 5 | Search a non-existing employee ("No Records Found") | EmployeeSearchTests |
| 6 | Open Add Employee page | AddEmployeeTests |
| 7 | Add employee with empty First Name | AddEmployeeTests |
| 8 | End-to-end: add employee + verify in list | AddEmployeeTests |
| 9 | Admin > Add User form fields | AdminTests |
| 10 | Footer "OrangeHRM, Inc" link opens orangehrm.com | UiTests |
| 11 | Sidebar menu contains all modules | UiTests |

## How to run
Requires JDK 17+, Maven 3.9+ and Chrome installed.

```bash
mvn clean test
```

Run headless (CI) by setting `headless=true` in `src/test/resources/config.properties`,
or switch `browser=firefox` / `browser=edge`.

## Allure report
```bash
mvn test
allure generate allure-results --clean -o allure-report
allure open allure-report
```
Commit `allure-report/`; do **not** commit `allure-results/`.

## Submission
Public GitHub repo named `automation-testing-project`, link emailed to
abdelrhman.route1@gmail.com with subject: `GP Task [Your Name] – G2`.
