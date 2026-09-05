package com.mirai.automation.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.mirai.automation.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.mirai.automation.config.Config;

public class HomePageSmokeTest {

    @Test
    public void shouldOpenStumbleGuysWebsite() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setHeadless(false));

            Page page = browser.newPage();
            page.navigate(Config.BASE_URL);

            HomePage homePage = new HomePage(page);
            homePage.acceptCookies();

            Assert.assertTrue(
                    page.url().contains("stumbleguys.com"),
                    "Stumble Guys website did not open successfully"
            );

            browser.close();
        }
    }
}