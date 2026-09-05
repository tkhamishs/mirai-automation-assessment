package com.mirai.automation.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mirai.automation.config.Config;

public class ProductDetailsModal {

    private final Locator purchaseButton;

    public ProductDetailsModal(Page page, String expectedPrice) {
        this.purchaseButton = page.locator(
                "button[class*='DetailsModal_modal__price_button__']"
        ).filter(
                new Locator.FilterOptions()
                        .setHasText(expectedPrice)
        );
    }

    public void waitUntilVisible() {
        purchaseButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(Config.DEFAULT_TIMEOUT.toMillis())
        );
    }

    public void purchase() {
        purchaseButton.click();
    }
}