package com.mirai.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginModal {

    private final Locator continueWithEmailButton;

    public LoginModal(Page page) {
        this.continueWithEmailButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue with email")
        );
    }

    public void continueWithEmail() {
        continueWithEmailButton.click();
    }
}