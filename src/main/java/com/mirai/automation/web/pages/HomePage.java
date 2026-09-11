package com.mirai.automation.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mirai.automation.config.Config;

public class HomePage {

    private final Page page;
    private final Locator acceptCookiesButton;
    private final Locator menuButton;
    private final Locator loginButton;

    public HomePage(Page page) {
        this.page = page;

        this.acceptCookiesButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Accept All")
                        .setExact(true)
        );

        this.menuButton = page.locator(
                "nav ul button:has(img[alt='avatar'])"
        );

        this.loginButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Login")
                        .setExact(true)
        );
    }

    public void acceptCookies() {
        try {
            acceptCookiesButton.waitFor(
                    new Locator.WaitForOptions()
                            .setState(
                                    WaitForSelectorState.VISIBLE
                            )
                            .setTimeout(
                                    Config.SHORT_TIMEOUT.toMillis()
                            )
            );

            acceptCookiesButton.click();

            acceptCookiesButton.waitFor(
                    new Locator.WaitForOptions()
                            .setState(
                                    WaitForSelectorState.HIDDEN
                            )
                            .setTimeout(
                                    Config.SHORT_TIMEOUT.toMillis()
                            )
            );

        } catch (PlaywrightException exception) {
            System.out.println(
                    "Cookie consent was not displayed."
            );
        }
    }

    public void openLogin() {
        menuButton.hover();

        loginButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(
                                WaitForSelectorState.VISIBLE
                        )
        );

        loginButton.click();
    }
}