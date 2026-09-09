package com.mirai.automation.web.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;
import com.mirai.automation.config.Config;
import com.mirai.automation.web.pages.HomePage;
import com.mirai.automation.web.pages.LoginModal;
import com.mirai.automation.web.pages.ScopelyAuthPage;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class SaveAuthStateTest {

    @Test
    public void saveAuthenticatedState() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium()
                     .launch(
                             new BrowserType.LaunchOptions()
                                     .setHeadless(false)
                                     .setSlowMo(700)
                     )) {

            BrowserContext context =
                    browser.newContext();

            Page page =
                    context.newPage();

            page.navigate(
                    Config.BASE_URL,
                    new Page.NavigateOptions()
                            .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
            );

            HomePage homePage =
                    new HomePage(page);

            homePage.acceptCookies();
            homePage.openLogin();

            LoginModal loginModal =
                    new LoginModal(page);

            loginModal.continueWithEmail();

            ScopelyAuthPage scopelyAuthPage =
                    new ScopelyAuthPage(page);

            scopelyAuthPage.enterEmail(
                    Config.TEST_EMAIL
            );

            scopelyAuthPage.continueLogin();

            System.out.println(
                    "Enter the OTP manually in the browser."
            );

            page.waitForURL(
                    url ->
                            url.contains("stumbleguys.com")
                                    && !url.contains("id.scopely.com"),
                    new Page.WaitForURLOptions()
                            .setTimeout(180000)
            );

            // Allow the authenticated Stumble Guys session to finish initializing.
            page.waitForTimeout(4000);

            context.storageState(
                    new BrowserContext.StorageStateOptions()
                            .setPath(Paths.get("auth-state.json"))
            );

            System.out.println(
                    "Authenticated state saved to auth-state.json"
            );

            page.waitForTimeout(5000);
        }
    }
}