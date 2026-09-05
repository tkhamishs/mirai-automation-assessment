package com.mirai.automation.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class LoginModal {

    private final Page page;
    private final Locator continueWithEmailButton;

    public LoginModal(Page page) {
        this.page = page;

        this.continueWithEmailButton = page.locator(
                "button:has(img[alt='Email logo'])"
        );
    }

    public void continueWithEmail() {
        continueWithEmailButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(30000)
        );

        long deadline = System.currentTimeMillis() + 30000;

        while (!page.url().contains("id.scopely.com")
                && System.currentTimeMillis() < deadline) {

            continueWithEmailButton.click();

            try {
                page.waitForURL(
                        url -> url.contains("id.scopely.com"),
                        new Page.WaitForURLOptions()
                                .setTimeout(3000)
                );
            } catch (com.microsoft.playwright.TimeoutError ignored) {
                // Retry while the page finishes initializing the login action.
            }
        }

        if (!page.url().contains("id.scopely.com")) {
            throw new RuntimeException(
                    "Scopely authentication page did not open after clicking Continue with email"
            );
        }
    }
}