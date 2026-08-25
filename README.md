# Automation Testing Project — OrangeHRM (Selenium + TestNG)

Automated UI tests for the OrangeHRM open-source demo, built with Selenium WebDriver 4, TestNG, and the Page Object Model (POM).

**Site under test:** https://opensource-demo.orangehrmlive.com/web/index.php/auth/login  
📊 **Live Allure Report:** [View Test Report Here](https://nada15122.github.io/automation-testing-project/allure-report/)

---

## 🛠️ Tech Stack
- **Language & Build:** Java 17, Maven
- **Automation Framework:** Selenium WebDriver 4 (built-in Selenium Manager)
- **Test Runner:** TestNG (Parallel Execution, DataProviders, IRetryAnalyzer)
- **Data Parsing:** Jackson (JSON Test Data Reader)
- **Logging:** Log4j2 (logged to `logs/automation.log`)
- **Reporting:** Allure Reports

---

## Project structure
```
automation-testing-project/
├── allure-report/                  
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/orangehrm/
│   │           ├── base/
│   │           │   └── BasePage.java
│   │           ├── pages/
│   │           │   ├── AddEmployeePage.java
│   │           │   ├── AdminPage.java
│   │           │   ├── DashboardPage.java
│   │           │   ├── LoginPage.java
│   │           │   └── PimPage.java
│   │           └── utils/
│   │               ├── ConfigReader.java
│   │               ├── DriverFactory.java
│   │               ├── JsonDataReader.java          
                    └── AllureUtils.java

│   └── test/
│       ├── java/
│       │   └── com/orangehrm/
│       │       ├── base/
│       │       │   └── BaseTest.java
│       │       ├── listeners/
│       │       │   ├── RetryAnalyzer.java
│       │       │   └── RetryListener.java
│       │       └── tests/
│       │           ├── AddEmployeeTests.java
│       │           ├── AdminTests.java
│       │           ├── EmployeeSearchTests.java
│       │           ├── LoginTests.java
│       │           └── UiTests.java
│       └── resources/
│           ├── config.properties   
│           ├── log4j2.xml          
│           └── testdata.json       
├── pom.xml                        
├── testng.xml                     
└── README.md                      
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
