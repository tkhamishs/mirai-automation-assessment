package com.mirai.automation.mobile.tests;

import com.mirai.automation.mobile.pages.MobileHomePage;
import com.mirai.automation.mobile.pages.MobileLoginModal;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class MobileWebSmokeTest {

    @Test
    public void shouldOpenScopelyEmailLoginInMobileChrome()
            throws MalformedURLException {

        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName("emulator-5554");

        options.setCapability("browserName", "Chrome");

        AndroidDriver driver = new AndroidDriver(
                new URL("http://127.0.0.1:4723"),
                options
        );

        try {
            driver.get("https://www.stumbleguys.com/");

            MobileHomePage mobileHomePage = new MobileHomePage(driver);

            mobileHomePage.acceptCookies();
            mobileHomePage.openMobileMenu();
            mobileHomePage.openLogin();

            MobileLoginModal mobileLoginModal =
                    new MobileLoginModal(driver);

            mobileLoginModal.continueWithEmail();

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(60));

            wait.until(
                    ExpectedConditions.urlContains("id.scopely.com")
            );

            Assert.assertTrue(
                    driver.getCurrentUrl().contains("id.scopely.com"),
                    "Scopely authentication page did not open on mobile web"
            );

            Assert.assertTrue(
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.xpath("//*[contains(text(), \"Let's get you in\")]")
                            )
                    ).isDisplayed(),
                    "Scopely login page was not displayed on mobile web"
            );

        } finally {
            driver.quit();
        }
    }
}