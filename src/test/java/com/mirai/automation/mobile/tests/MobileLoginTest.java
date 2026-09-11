package com.mirai.automation.mobile.tests;

import com.mirai.automation.config.Config;
import com.mirai.automation.mobile.base.BaseMobileTest;
import com.mirai.automation.mobile.pages.MobileHomePage;
import com.mirai.automation.mobile.pages.MobileLoginModal;
import com.mirai.automation.mobile.pages.MobileScopelyAuthPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MobileLoginTest extends BaseMobileTest {

    @Test
    public void shouldReachEmailVerificationScreenInMobileChrome() {

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

        WebDriverWait wait =
                new WebDriverWait(
                        driver,
                        Config.DEFAULT_TIMEOUT
                );

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

        Assert.assertTrue(
                scopelyAuthPage
                        .getVerificationMessage()
                        .contains(
                                Config.TEST_EMAIL
                        ),
                "Verification message does not contain the expected email"
        );
    }
}