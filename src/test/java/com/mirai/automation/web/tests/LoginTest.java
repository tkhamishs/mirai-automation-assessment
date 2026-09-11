package com.mirai.automation.web.tests;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import com.mirai.automation.config.Config;
import com.mirai.automation.web.base.BaseWebTest;
import com.mirai.automation.web.pages.HomePage;
import com.mirai.automation.web.pages.LoginModal;
import com.mirai.automation.web.pages.ScopelyAuthPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseWebTest {

    @Test
    public void shouldNavigateToEmailVerificationScreen() {

        page.navigate(
                Config.BASE_URL,
                new Page.NavigateOptions()
                        .setWaitUntil(
                                WaitUntilState.DOMCONTENTLOADED
                        )
        );

        HomePage homePage =
                new HomePage(
                        page
                );

        homePage.acceptCookies();

        homePage.openLogin();

        LoginModal loginModal =
                new LoginModal(
                        page
                );

        loginModal.continueWithEmail();

        page.waitForURL(
                url -> url.contains(
                        "id.scopely.com"
                ),
                new Page.WaitForURLOptions()
                        .setTimeout(
                                Config.DEFAULT_TIMEOUT.toMillis()
                        )
        );

        ScopelyAuthPage scopelyAuthPage =
                new ScopelyAuthPage(
                        page
                );

        scopelyAuthPage.enterEmail(
                Config.TEST_EMAIL
        );

        Assert.assertEquals(
                scopelyAuthPage.getEmailValue(),
                Config.TEST_EMAIL,
                "Email was not entered correctly"
        );

        scopelyAuthPage.continueLogin();

        Assert.assertTrue(
                scopelyAuthPage.isVerificationScreenVisible(),
                "Email verification screen was not displayed"
        );
    }
}