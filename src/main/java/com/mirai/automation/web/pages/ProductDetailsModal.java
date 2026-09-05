package com.mirai.automation.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ProductDetailsModal {

    private final Locator modal;

    public ProductDetailsModal(Page page, String expectedPrice) {
        this.modal = page.getByRole(AriaRole.DIALOG)
                .filter(new Locator.FilterOptions().setHasText(expectedPrice));
    }

    public void waitUntilVisible() {
        modal.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(30000)
        );
    }

    public boolean isVisible() {
        return modal.isVisible();
    }
}