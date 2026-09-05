package com.mirai.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class HomePage {

    private final Page page;
    private final Locator acceptCookiesButton;
    private final Locator menuButton;
    private final Locator loginButton;

    public HomePage(Page page) {
        this.page = page;

        this.acceptCookiesButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Accept All")
        );

        this.menuButton = page.locator(
                "nav ul button:has(img[alt='avatar'])"
        );

        this.loginButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Login")
        );
    }

    public void acceptCookies() {
        acceptCookiesButton.click();
    }

    public void openLogin() {
        menuButton.hover();

        loginButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        loginButton.click();
    }
}