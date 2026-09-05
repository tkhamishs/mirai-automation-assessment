package com.mirai.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HomePage {

    private final Page page;
    private final Locator acceptCookiesButton;

    public HomePage(Page page) {
        this.page = page;
        this.acceptCookiesButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Accept All")
        );
    }

    public void acceptCookies() {
        acceptCookiesButton.click();
    }
}