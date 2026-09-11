package com.mirai.automation.mobile.tests;

import com.mirai.automation.config.Config;
import com.mirai.automation.mobile.base.BaseMobileTest;
import com.mirai.automation.mobile.pages.MobileCheckoutPage;
import com.mirai.automation.mobile.pages.MobileHomePage;
import com.mirai.automation.mobile.pages.MobileLoginModal;
import com.mirai.automation.mobile.pages.MobileProductCard;
import com.mirai.automation.mobile.pages.MobileProductDetailsModal;
import com.mirai.automation.mobile.pages.MobileScopelyAuthPage;
import com.mirai.automation.mobile.pages.MobileShopPage;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class MobilePurchaseTest extends BaseMobileTest {

    @Override
    protected UiAutomator2Options createOptions() {

        UiAutomator2Options options =
                super.createOptions();

        options.setNoReset(
                true
        );

        return options;
    }

    @Test
    public void shouldCompleteMobilePurchaseFlowUntilPaymentConfirmation() {

        WebDriverWait wait =
                new WebDriverWait(
                        driver,
                        Config.CHECKOUT_TIMEOUT
                );

        WebDriverWait manualOtpWait =
                new WebDriverWait(
                        driver,
                        Config.MANUAL_OTP_TIMEOUT
                );

        driver.get(
                Config.BASE_URL
        );

        MobileHomePage mobileHomePage =
                new MobileHomePage(
                        driver
                );

        mobileHomePage.acceptCookies();
        mobileHomePage.openMobileMenu();
        mobileHomePage.openLogin();

        MobileLoginModal mobileLoginModal =
                new MobileLoginModal(
                        driver
                );

        mobileLoginModal.continueWithEmail();

        wait.until(
                ExpectedConditions.urlContains(
                        "id.scopely.com"
                )
        );

        MobileScopelyAuthPage scopelyAuthPage =
                new MobileScopelyAuthPage(
                        driver
                );

        scopelyAuthPage.enterEmail(
                Config.TEST_EMAIL
        );

        Assert.assertEquals(
                scopelyAuthPage.getEmailValue(),
                Config.TEST_EMAIL,
                "Email was not entered correctly on mobile web"
        );

        scopelyAuthPage.continueLogin();

        Assert.assertTrue(
                scopelyAuthPage.isVerificationScreenVisible(),
                "Verification screen was not displayed on mobile web"
        );

        System.out.println(
                "Waiting for manual OTP verification..."
        );

        manualOtpWait.until(
                ExpectedConditions.urlMatches(
                        "^https://(www\\.)?stumbleguys\\.com(/.*)?$"
                )
        );

        System.out.println(
                "Login completed. Waiting for authenticated UI..."
        );

        By loggedInAvatar =
                By.cssSelector(
                        "img[alt='avatar'][src*='logged_in']"
                );

        wait.until(webDriver -> {

            List<WebElement> avatars =
                    driver.findElements(
                            loggedInAvatar
                    );

            return avatars.stream()
                    .anyMatch(
                            WebElement::isDisplayed
                    );
        });

        driver.get(
                Config.BASE_URL + "shop"
        );

        MobileShopPage shopPage =
                new MobileShopPage(
                        driver
                );

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

        String expectedAmount =
                productPrice.replaceAll(
                        "[^0-9.,]",
                        ""
                );

        product.open();

        MobileProductDetailsModal productDetailsModal =
                new MobileProductDetailsModal(
                        driver,
                        productPrice
                );

        productDetailsModal.waitUntilVisible();

        productDetailsModal.purchase();

        MobileCheckoutPage checkoutPage =
                new MobileCheckoutPage(
                        driver
                );

        checkoutPage.selectCardPayment();

        Assert.assertTrue(
                checkoutPage.isPayWithCardVisible(),
                "Pay with Card heading is not visible"
        );

        Assert.assertTrue(
                checkoutPage.isCardPaymentFormVisible(),
                "Card payment form is not visible"
        );

        Assert.assertTrue(
                checkoutPage.isCardNumberFieldVisible(),
                "Card number field is not visible"
        );

        Assert.assertTrue(
                checkoutPage.isExpiryFieldVisible(),
                "Expiry field is not visible"
        );

        Assert.assertTrue(
                checkoutPage.isCvcFieldVisible(),
                "CVC field is not visible"
        );

        Assert.assertTrue(
                checkoutPage.isEmailFieldVisible(),
                "Receipt email field is not visible"
        );

        Assert.assertEquals(
                checkoutPage.getSubtotalAmount(),
                expectedAmount,
                "Mobile checkout subtotal amount does not match the selected product price"
        );

        Assert.assertEquals(
                checkoutPage.getSubtotalCurrency(),
                "SAR",
                "Mobile checkout subtotal currency is incorrect"
        );

        Assert.assertEquals(
                checkoutPage.getTotalAmount(),
                expectedAmount,
                "Mobile checkout total amount does not match the selected product price"
        );

        Assert.assertEquals(
                checkoutPage.getTotalCurrency(),
                "SAR",
                "Mobile checkout total currency is incorrect"
        );

        Assert.assertTrue(
                checkoutPage.isPayButtonVisible(),
                "Pay button is not visible on mobile checkout"
        );
    }
}