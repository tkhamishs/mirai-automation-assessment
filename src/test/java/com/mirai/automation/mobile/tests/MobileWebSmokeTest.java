package com.mirai.automation.mobile.tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import com.mirai.automation.mobile.pages.MobileHomePage;
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
    public void shouldOpenLoginModalInMobileChrome() throws MalformedURLException {
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

            WebDriverWait wait =
                    new WebDriverWait(driver, Duration.ofSeconds(30));

            boolean emailLoginOptionVisible = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("button:has(img[alt='Email logo'])")
                    )
            ).isDisplayed();

            Assert.assertTrue(
                    emailLoginOptionVisible,
                    "Login modal was not displayed on mobile web"
            );
        } finally {
            driver.quit();
        }
    }
}