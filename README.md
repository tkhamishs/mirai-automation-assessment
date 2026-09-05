# Mirai QA Automation Assessment

Automation project for the QA Automation Lead assessment using:

https://www.stumbleguys.com/

Covered flows:

- Desktop Web Login
- Desktop Web Purchase
- Android Mobile Web Login
- Android Mobile Web Purchase

## Tech Stack

- Java 21
- Maven
- TestNG
- Playwright
- Selenium WebDriver
- Appium 2
- UiAutomator2

## Project Structure

```text
src
├── main
│   └── java
│       └── com.mirai.automation
│           ├── config
│           ├── web.pages
│           └── mobile.pages
└── test
    └── java
        └── com.mirai.automation
            ├── web.tests
            └── mobile.tests
```

Page Object Model is used to keep page actions separate from the test scenarios.

## Prerequisites

- Java 21
- Maven
- Node.js
- Appium 2
- Android Studio
- Android Emulator
- Chrome installed on the emulator

Quick checks:

```bash
java -version
mvn -version
appium -v
adb devices
```

## Desktop Setup

Install Playwright Chromium:

```bash
mvn exec:java \
-Dexec.mainClass=com.microsoft.playwright.CLI \
-Dexec.args="install chromium"
```

## Mobile Setup

Tested with:

- Pixel 9 Android Emulator
- Android 17
- Chrome 149.0.7827.5
- Appium 2.15.0
- UiAutomator2 4.2.8

Make sure the emulator is running:

```bash
adb devices
```

Start Appium:

```bash
appium --allow-insecure uiautomator2:chromedriver_autodownload
```

The `chromedriver_autodownload` option is needed because the emulator Chrome version requires a compatible ChromeDriver.

## Run Tests

All tests:

```bash
mvn test
```

Desktop Login:

```bash
mvn -Dtest=LoginTest test
```

Desktop Purchase:

```bash
mvn -Dtest=PurchaseTest test
```

Mobile Login:

```bash
mvn -Dtest=MobileLoginTest test
```

Mobile Purchase:

```bash
mvn -Dtest=MobilePurchaseTest test
```

## Login Flow

The login tests cover:

1. Open Stumble Guys
2. Accept cookies
3. Open Login
4. Continue with email
5. Redirect to Scopely
6. Enter the test email
7. Continue to the email verification page
8. Verify the email verification screen

The automation stops at the email verification step. The OTP is sent to an external mailbox and is not handled by the automation.

## Purchase Flow

The purchase tests:

1. Open the shop
2. Find the first available product
3. Read the product price
4. Open the product details
5. Verify the same price
6. Click the purchase button
7. Verify that Login is requested

The product is selected dynamically instead of using a hardcoded product.

No payment is completed.

## Known Authentication Issue

While testing the live website, I found an issue with the login session.

After completing the Scopely email verification successfully, the browser returns to Stumble Guys, but the site still shows the user as logged out.

Starting a purchase asks for Login again.

I reproduced the same behavior on:

- Desktop Web
- Android Mobile Web

Because of this, the purchase automation cannot continue as an authenticated user to the payment step.

The tests currently validate the purchase flow up to this blocker.

The issue appears to be related to the authenticated session not being persisted or recognized after returning from Scopely, but the exact root cause was not investigated from the backend side.

## Wait Strategy

No implicit waits or `Thread.sleep()` are used.

Desktop:

- Playwright auto-waiting
- Visibility checks
- URL conditions

Mobile:

- `WebDriverWait`
- Selenium expected conditions

A retry is used in the desktop email-login navigation because the Login modal can become visible before the click action is fully ready.

## Notes

- Desktop browser: Chromium
- Mobile browser: Chrome on Android Emulator
- Mobile Web is automated through Appium + Selenium
- `auth-state.json` is excluded from Git because it may contain session cookies or tokens