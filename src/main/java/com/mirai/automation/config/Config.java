package com.mirai.automation.config;

import java.time.Duration;

public class Config {

    public static final String BASE_URL =
            "https://www.stumbleguys.com/";

    public static final String TEST_EMAIL =
            "tarek_ce@hotmail.com";

    public static final String TEST_CARD_NUMBER =
            "4242424242424242";

    public static final String TEST_CARD_EXPIRY =
            "12/30";

    public static final String TEST_CARD_CVC =
            "123";

    public static final String APPIUM_SERVER_URL =
            "http://127.0.0.1:4723";

    public static final String MOBILE_DEVICE_NAME =
            "emulator-5554";

    public static final String MOBILE_BROWSER_NAME =
            "Chrome";

    public static final Duration DEFAULT_TIMEOUT =
            Duration.ofSeconds(60);

    public static final Duration CHECKOUT_TIMEOUT =
            Duration.ofSeconds(120);
}