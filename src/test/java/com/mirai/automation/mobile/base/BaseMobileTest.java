package com.mirai.automation.mobile.base;

import com.mirai.automation.config.Config;
import com.mirai.automation.review.MobileTestContext;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseMobileTest {

    protected AndroidDriver driver;

    @BeforeMethod
    public void setUpMobileDriver()
            throws MalformedURLException {

        UiAutomator2Options options =
                createOptions();

        driver =
                new AndroidDriver(
                        new URL(
                                Config.APPIUM_SERVER_URL
                        ),
                        options
                );

        MobileTestContext.setDriver(
                driver
        );
    }

    protected UiAutomator2Options createOptions() {

        UiAutomator2Options options =
                new UiAutomator2Options()
                        .setDeviceName(
                                Config.MOBILE_DEVICE_NAME
                        );

        options.setCapability(
                "browserName",
                Config.MOBILE_BROWSER_NAME
        );

        return options;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMobileDriver() {

        MobileTestContext.clear();

        if (driver != null) {
            driver.quit();
        }
    }
}