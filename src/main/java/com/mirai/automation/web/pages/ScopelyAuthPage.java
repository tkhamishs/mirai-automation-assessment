package com.mirai.automation.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mirai.automation.config.Config;

public class ScopelyAuthPage {

    private final Locator pageHeading;
    private final Locator emailField;
    private final Locator continueButton;
    private final Locator verificationHeading;
    private final Locator verificationMessage;

    public ScopelyAuthPage(Page page) {
        this.pageHeading = page.getByText("Let's get you in");

        this.emailField = page.locator("input").first();

        this.continueButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Continue")
                        .setExact(true)
        );

        this.verificationHeading = page.getByText("Check your inbox!");

        this.verificationMessage = page.getByText("We've sent a code to");
    }

    public void enterEmail(String email) {
        pageHeading.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(Config.DEFAULT_TIMEOUT.toMillis())
        );

        emailField.fill(email);
    }

    public String getEmailValue() {
        return emailField.inputValue();
    }

    public void continueLogin() {
        continueButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(Config.DEFAULT_TIMEOUT.toMillis())
        );

        continueButton.click();

        verificationHeading.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(Config.DEFAULT_TIMEOUT.toMillis())
        );
    }

    public boolean isVerificationScreenVisible() {
        return verificationHeading.isVisible();
    }

    public String getVerificationMessage() {
        return verificationMessage.innerText();
    }
}