package com.mirai.automation.mobile.tests;

import com.mirai.automation.config.Config;
import com.mirai.automation.mobile.pages.MobileHomePage;
import com.mirai.automation.mobile.pages.MobileLoginModal;
import com.mirai.automation.mobile.pages.MobileProductCard;
import com.mirai.automation.mobile.pages.MobileProductDetailsModal;
import com.mirai.automation.mobile.pages.MobileShopPage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class MobilePurchaseTest {

    @Test
    public void shouldPromptForLoginWhenStartingPurchase()
            throws MalformedURLException {

        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName("emulator-5554");

        options.setCapability("browserName", "Chrome");

        AndroidDriver driver = new AndroidDriver(
                new URL("http://127.0.0.1:4723"),
                options
        );

        try {
            driver.get(Config.BASE_URL + "shop");

            MobileHomePage mobileHomePage =
                    new MobileHomePage(driver);

            mobileHomePage.acceptCookies();

            MobileShopPage shopPage =
                    new MobileShopPage(driver);

            Assert.assertTrue(
                    shopPage.getAvailableProductCount() > 0,
                    "No purchasable products were found in the mobile shop"
            );

            MobileProductCard product =
                    shopPage.getFirstAvailableProduct();

            String productPrice =
                    product.getPrice();

            Assert.assertTrue(
                    productPrice.contains("SAR"),
                    "Selected mobile product does not have a valid SAR price"
            );

            product.open();

            MobileProductDetailsModal productDetailsModal =
                    new MobileProductDetailsModal(
                            driver,
                            productPrice
                    );

            productDetailsModal.waitUntilVisible();
            productDetailsModal.purchase();

            MobileLoginModal loginModal =
                    new MobileLoginModal(driver);

            Assert.assertTrue(
                    loginModal.isVisible(),
                    "Login modal was not displayed after starting the mobile purchase"
            );

        } finally {
            driver.quit();
        }
    }
}