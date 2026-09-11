package com.mirai.automation.web.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitForSelectorState;
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

        try (
                Playwright playwright = Playwright.create();

                Browser browser = playwright.chromium().launch(
                        new BrowserType.LaunchOptions()
                                .setHeadless(
                                        Config.HEADLESS
                                )
                )
        ) {

            BrowserContext context =
                    browser.newContext();

            Page page =
                    context.newPage();

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

            ScopelyAuthPage scopelyAuthPage =
                    new ScopelyAuthPage(
                            page
                    );

            scopelyAuthPage.enterEmail(
                    Config.TEST_EMAIL
            );

            scopelyAuthPage.continueLogin();

            System.out.println(
                    "Enter the OTP manually in the browser."
            );

            page.waitForURL(
                    url -> url.matches(
                            "^https://(www\\.)?stumbleguys\\.com(/.*)?$"
                    ),
                    new Page.WaitForURLOptions()
                            .setTimeout(
                                    Config.MANUAL_OTP_TIMEOUT.toMillis()
                            )
            );

            Locator loggedInAvatar =
                    page.locator(
                                    "img[alt='avatar'][src*='logged_in']:visible"
                            )
                            .first();

            loggedInAvatar.waitFor(
                    new Locator.WaitForOptions()
                            .setState(
                                    WaitForSelectorState.VISIBLE
                            )
                            .setTimeout(
                                    Config.DEFAULT_TIMEOUT.toMillis()
                            )
            );

            context.storageState(
                    new BrowserContext.StorageStateOptions()
                            .setPath(
                                    Paths.get(
                                            "auth-state.json"
                                    )
                            )
            );

            System.out.println(
                    "Authenticated state saved to auth-state.json"
            );
        }
    }
}