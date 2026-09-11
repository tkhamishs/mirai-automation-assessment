# Mirai QA Automation Assessment

This project contains the automation framework I created for the Mirai / Scopely QA Automation Lead assessment.

It covers the Stumble Guys web store on:

- Desktop Web using Playwright
- Android Mobile Web using Appium and Selenium

The implemented scenarios are:

1. Login flow
2. Purchase flow up to the payment confirmation stage

The purchase flow intentionally stops before any real transaction is performed.

---

## Technology Stack

- Java 21
- Maven
- TestNG
- Playwright Java
- Selenium
- Appium
- UiAutomator2
- Android Chrome

---

## Framework Structure

```text
mirai-automation-assessment/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/
    │   └── java/
    │       └── com/mirai/automation/
    │           ├── config/
    │           │   └── Config.java
    │           │
    │           ├── web/pages/
    │           │   ├── CheckoutPage.java
    │           │   ├── HomePage.java
    │           │   ├── LoginModal.java
    │           │   ├── ProductCard.java
    │           │   ├── ProductDetailsModal.java
    │           │   ├── ScopelyAuthPage.java
    │           │   └── ShopPage.java
    │           │
    │           ├── mobile/pages/
    │           │   ├── MobileCheckoutPage.java
    │           │   ├── MobileHomePage.java
    │           │   ├── MobileLoginModal.java
    │           │   ├── MobileProductCard.java
    │           │   ├── MobileProductDetailsModal.java
    │           │   ├── MobileScopelyAuthPage.java
    │           │   └── MobileShopPage.java
    │           │
    │           └── review/
    │               ├── FailureClassifier.java
    │               ├── ReviewReport.java
    │               ├── ReviewReportWriter.java
    │               └── TestReviewAgent.java
    │
    └── test/
        ├── java/
        │   └── com/mirai/automation/
        │       ├── web/
        │       │   ├── base/
        │       │   │   └── BaseWebTest.java
        │       │   └── tests/
        │       │       ├── LoginTest.java
        │       │       ├── PurchaseTest.java
        │       │       └── SaveAuthStateTest.java
        │       │
        │       ├── mobile/
        │       │   ├── base/
        │       │   │   └── BaseMobileTest.java
        │       │   └── tests/
        │       │       ├── MobileLoginTest.java
        │       │       └── MobilePurchaseTest.java
        │       │
        │       └── review/
        │           ├── MobileTestContext.java
        │           ├── TestContext.java
        │           ├── TestReviewAgentTest.java
        │           └── TestReviewListener.java
        │
        └── resources/
            └── META-INF/services/
                └── org.testng.ITestNGListener
Design Approach

The framework follows the Page Object Model.

The test classes contain the scenarios and assertions, while the page objects contain the UI-specific implementation such as:

locators
waits
browser interactions
iframe handling
reusable page actions

Desktop and mobile have separate page objects because Playwright and Appium/Selenium use different APIs, and the UI can behave differently between desktop and mobile.

Common browser and driver setup is handled in:

BaseWebTest
BaseMobileTest

This keeps setup and teardown logic out of the individual tests and avoids duplication.

Desktop Web

Desktop automation uses Playwright with Chromium.

Desktop Login Flow

Run:

mvn -Dtest=LoginTest test

The test:

Opens Stumble Guys
Handles cookie consent
Opens Login
Selects Continue with Email
Navigates to Scopely authentication
Enters the configured email address
Continues to the OTP verification screen
Verifies that the OTP screen is displayed

OTP entry is manual.

Desktop Authentication for Purchase Tests

PurchaseTest uses a saved Playwright browser session stored in:

auth-state.json

This file is excluded from Git.

A valid authenticated state must be created before running the desktop purchase test.

Recommended Desktop Sequence
1. Verify the Login Flow
mvn -Dtest=LoginTest test

This verifies that the login flow reaches the OTP screen correctly.

2. Save the Authenticated Session

Run:

mvn -Dtest=SaveAuthStateTest test

When the OTP screen appears, enter the OTP manually.

After successful authentication, the test waits until the logged-in Stumble Guys UI is visible and then saves the browser state to:

auth-state.json

Successful output:

Authenticated state saved to auth-state.json
3. Run the Purchase Test
mvn -Dtest=PurchaseTest test

PurchaseTest loads the saved authentication state before opening the shop.

If auth-state.json is missing, expired, or was not created after successful OTP verification, the application may redirect back to Login.

Desktop Purchase Flow

The test selects an available product dynamically instead of depending on a hardcoded product.

The flow verifies:

Shop is accessible
At least one purchasable product is available
Product price can be read
Product details are opened
Checkout opens successfully
Payment type section is visible
Card payment is selected
Card number field is visible
Expiry field is visible
CVC field is visible
Receipt email field is visible
Checkout subtotal matches the selected product price
Checkout currency is SAR
Checkout total matches the selected product price
Pay button is visible

The test stops at this point.

No card details are entered and the Pay button is not clicked.

Checkout and iframe Handling

The checkout page contains content from multiple embedded documents rather than one normal HTML page.

The structure is roughly:

Stumble Guys Page
│
└── Xsolla Payment iframe
    │
    ├── Payment options
    ├── Subtotal
    ├── Total
    ├── Pay button
    │
    ├── Stripe Secure iframe
    │   ├── Card Number
    │   ├── Expiry
    │   └── CVC
    │
    └── Secure Email iframe
        └── Receipt Email

The automation has to switch to the correct iframe before interacting with the elements inside it.

For mobile Selenium/Appium automation, the framework switches between:

default content
→ payment iframe
→ Stripe iframe

and:

default content
→ payment iframe
→ email iframe

The payment UI can also re-render after changing the payment method. Because of that, frame references are located again when needed instead of relying on an older reference that may have become stale.

Android Mobile Web

Mobile Web automation runs through:

Appium
→ UiAutomator2
→ Android Emulator
→ Chrome
→ Selenium WebDriver

Tested setup:

Android Emulator: Pixel 9
Browser: Chrome
Start Appium

Start the Appium server before running the mobile tests:

appium --allow-insecure uiautomator2:chromedriver_autodownload

ChromeDriver auto-download is enabled because the Chrome version installed on the emulator must have a compatible ChromeDriver version.

Mobile Login Flow

Run:

mvn -Dtest=MobileLoginTest test

The test verifies:

Stumble Guys opens in Chrome
Cookie consent is handled
Mobile menu opens
Login is selected
Continue with Email is selected
Scopely authentication opens
Email is entered
OTP verification screen is displayed
Verification text contains the expected email address
Mobile Purchase Flow

Run:

mvn -Dtest=MobilePurchaseTest test

The mobile purchase test performs login and purchase in the same Appium browser session.

When the OTP screen appears, enter the OTP manually.

The test then waits for both:

Authenticated Stumble Guys URL
+
Authenticated avatar

before continuing.

The full flow is:

Login
→ Manual OTP
→ Authenticated UI
→ Shop
→ Product selection
→ Product details
→ Checkout
→ Card payment
→ Card form validation
→ Price validation
→ Stop before payment

No card information is entered and payment is not confirmed.

Authentication Prerequisite

During testing, it was clarified that the Scopely account needs to have an existing Stumble Guys game state created through the mobile game.

Without this prerequisite, successful Scopely authentication may not result in the expected authenticated web-store session.

Why OTP Is Manual

The OTP mailbox is outside the application under test.

The framework does not store:

mailbox passwords
personal email credentials
hardcoded OTP values
provider-specific email credentials

In a controlled test environment, OTP retrieval could be automated using a dedicated test mailbox or an approved email service.

For this assessment, OTP is entered manually to avoid storing personal mailbox credentials or coupling the test framework to an external email provider.

Synchronization Strategy

The final framework does not use fixed sleeps for synchronization.

There is no:

Thread.sleep(...)

and no:

waitForTimeout(...)

The tests wait for actual application conditions instead, including:

element visibility
element clickability
URL changes
iframe availability
authenticated UI state
product availability
checkout visibility
logged-in avatar visibility

Timeout values are centralized in:

Config.java

This makes synchronization easier to maintain and avoids relying on arbitrary delays.

Failure Review Agent

I added a small failure review agent to help with first-level triage when a test fails.

The flow is:

Test failure
    ↓
TestNG Listener
    ↓
Failure evidence captured
    ↓
FailureClassifier
    ↓
TestReviewAgent
    ↓
ReviewReport
    ↓
Console output + report file

The agent looks at the failure message, assigns a likely category, suggests a next action, and writes the result to a report.

Example:

========== QA REVIEW ==========

Test Name: shouldCompletePurchaseFlowUntilPaymentConfirmation
Status: FAIL
Classification: Potential flaky issue
Reason: The failure contains signs of a timing, stale element, or synchronization problem.
Flaky Risk: High
Next Action: Check synchronization, element re-rendering, and whether the failure reproduces consistently.

===============================

Reports are stored under:

target/qa-review/
Failure Classification

Possible classifications include:

Potential flaky issue
Environment issue
Product issue
Automation issue
Needs investigation

For example:

Timeout waiting for iframe

can be classified as:

Potential flaky issue

because timing or synchronization failures can sometimes indicate flaky behavior.

The classification is only an initial triage suggestion.

A single timeout does not confirm that a test is flaky. Repeated execution history, reproducibility, environment state and application behavior still need to be checked.

Failure Screenshots

Screenshots are automatically captured when a desktop or mobile test fails.

They are stored under:

target/qa-review/screenshots/

Example desktop screenshot:

shouldCompletePurchaseFlowUntilPaymentConfirmation-web.png

Example mobile screenshot:

shouldReachEmailVerificationScreenInMobileChrome-mobile.png

Desktop screenshots are captured through Playwright.

Mobile screenshots are captured through Appium/Selenium TakesScreenshot.

The screenshot is supporting evidence only. It does not affect the failure classification.

Its purpose is to show the application state at the moment the test failed so the engineer investigating the issue has more context.

Failure Screenshot Lifecycle

Browser and driver lifecycle is handled through:

BaseWebTest
BaseMobileTest

The test lifecycle is:

@BeforeMethod
    ↓
Create browser / driver
    ↓
Store current Page / AndroidDriver
    ↓
Run test
    ↓
Failure occurs
    ↓
TestNG listener captures screenshot
    ↓
Failure review is generated
    ↓
@AfterMethod
    ↓
Browser / driver is closed

Keeping the browser or driver alive until after the listener finishes allows failure evidence to be captured before teardown.

Configuration

Common values are stored in:

src/main/java/com/mirai/automation/config/Config.java

Configuration includes:

Stumble Guys base URL
test email
Appium server URL
Android device name
mobile browser name
Playwright headless setting
common timeouts
checkout timeout
manual OTP timeout

Centralizing these values avoids repeating configuration throughout the test and page classes.

Running Individual Tests

Desktop Login:

mvn -Dtest=LoginTest test

Save Desktop Authentication:

mvn -Dtest=SaveAuthStateTest test

Desktop Purchase:

mvn -Dtest=PurchaseTest test

Mobile Login:

mvn -Dtest=MobileLoginTest test

Mobile Purchase:

mvn -Dtest=MobilePurchaseTest test

Failure Review Agent Test:

mvn -Dtest=TestReviewAgentTest test

Compile without running tests:

mvn -DskipTests test-compile
Test Recordings

Successful automation recordings are available here:

https://drive.google.com/drive/folders/1_uFk7-hG6GXncnud7SX705VZ9e1kGcX6?usp=sharing

The recordings cover:

Desktop Login Flow
Desktop Purchase Flow
Mobile Login Flow
Mobile Purchase Flow
Security and Safety

The following file is not committed:

auth-state.json

It is included in .gitignore.

The framework also does not contain:

OTP values
mailbox passwords
payment card data
private certificates
external service credentials

The checkout automation stops before any real transaction is performed.

Known Limitations
Manual OTP

OTP verification requires manual user interaction.

Desktop Authentication State

Desktop purchase execution requires a valid auth-state.json.

Mobile Authentication

Mobile purchase performs login and manual OTP in the same Appium session.

External Payment Provider

Checkout uses Xsolla and Stripe-hosted iframe components. Their DOM structure and loading behavior are outside the direct control of the Stumble Guys application.

Emulator / ChromeDriver Compatibility

Android Chrome requires a compatible ChromeDriver. Appium is started with ChromeDriver auto-download enabled to support the browser version installed on the emulator.

Failure Review Agent

The failure review agent is intended to help with first-level investigation only. Its classification should not be treated as confirmed root cause without reviewing the actual failure evidence.

Repository

GitHub:

https://github.com/tkhamishs/mirai-automation-assessment

Summary

The main goal was to keep the test scenarios readable while separating browser interaction details from the test logic.

The main design choices are:

Page Object Model
separate desktop and mobile page objects
reusable browser and driver lifecycle
centralized configuration
condition-based synchronization
meaningful assertions
safe handling of authentication
safe handling of live checkout
automatic failure screenshots
failure review agent for first-level triage