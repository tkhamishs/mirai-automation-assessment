package com.mirai.automation.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import com.mirai.automation.config.Config;
import com.mirai.automation.pages.HomePage;
import com.mirai.automation.pages.LoginModal;
import com.mirai.automation.pages.ScopelyAuthPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageSmokeTest {

    @Test
    public void shouldOpenStumbleGuysWebsite() {
        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setHeadless(false));

            Page page = browser.newPage();

            page.navigate(
                    Config.BASE_URL,
                    new Page.NavigateOptions()
                            .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
            );

            HomePage homePage = new HomePage(page);
            homePage.acceptCookies();
            homePage.openLogin();

            LoginModal loginModal = new LoginModal(page);
            loginModal.continueWithEmail();

            ScopelyAuthPage scopelyAuthPage = new ScopelyAuthPage(page);

            String email = "tarek_ce@hotmail.com";
            scopelyAuthPage.enterEmail(email);

            Assert.assertEquals(
                    scopelyAuthPage.getEmailValue(),
                    email,
                    "Email was not entered correctly"
            );

            browser.close();
        }
    }
}