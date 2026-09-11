package com.mirai.automation.review;

import io.appium.java_client.android.AndroidDriver;

public class MobileTestContext {

    private static final ThreadLocal<AndroidDriver> CURRENT_DRIVER =
            new ThreadLocal<>();

    public static void setDriver(AndroidDriver driver) {
        CURRENT_DRIVER.set(driver);
    }

    public static AndroidDriver getDriver() {
        return CURRENT_DRIVER.get();
    }

    public static void clear() {
        CURRENT_DRIVER.remove();
    }

    private MobileTestContext() {
    }
}