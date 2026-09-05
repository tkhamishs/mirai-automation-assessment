package com.mirai.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ScopelyAuthPage {

    private final Locator pageHeading;
    private final Locator emailField;

    public ScopelyAuthPage(Page page) {
        this.pageHeading = page.getByText("Let's get you in");
        this.emailField = page.locator("input").first();
    }

    public void enterEmail(String email) {
        pageHeading.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(30000)
        );

        emailField.fill(email);
    }

    public String getEmailValue() {
        return emailField.inputValue();
    }
}