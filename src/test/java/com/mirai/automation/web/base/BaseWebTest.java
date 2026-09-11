package com.mirai.automation.web.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.mirai.automation.config.Config;
import com.mirai.automation.review.TestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseWebTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod
    public void setUpBrowser() {

        playwright =
                Playwright.create();

        browser =
                playwright.chromium().launch(
                        new BrowserType.LaunchOptions()
                                .setHeadless(
                                        Config.HEADLESS
                                )
                );

        context =
                createBrowserContext();

        page =
                context.newPage();

        TestContext.setPage(
                page
        );
    }

    protected BrowserContext createBrowserContext() {

        return browser.newContext();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownBrowser() {

        TestContext.clear();

        if (context != null) {
            context.close();
        }

        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }
}