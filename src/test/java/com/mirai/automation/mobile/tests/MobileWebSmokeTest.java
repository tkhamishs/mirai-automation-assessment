package com.mirai.automation.mobile.tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class MobileWebSmokeTest {

    @Test
    public void shouldOpenStumbleGuysInMobileChrome() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName("emulator-5554");

        options.setCapability("browserName", "Chrome");

        AndroidDriver driver = new AndroidDriver(
                new URL("http://127.0.0.1:4723"),
                options
        );

        try {
            driver.get("https://www.stumbleguys.com/");

            Assert.assertTrue(
                    driver.getCurrentUrl().contains("stumbleguys.com"),
                    "Stumble Guys website did not open in mobile Chrome"
            );
        } finally {
            driver.quit();
        }
    }
}