package com.mirai.automation.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.mirai.automation.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageSmokeTest {

    @Test
    public void shouldOpenStumbleGuysWebsite() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions().setHeadless(false));

            Page page = browser.newPage();
            page.navigate("https://www.stumbleguys.com/");

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