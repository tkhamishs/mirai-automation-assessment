package com.mirai.automation.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ProductDetailsModal {

    private final Locator purchaseButton;

    public ProductDetailsModal(Page page, String expectedPrice) {
        this.purchaseButton = page.locator(
                "button[class*='DetailsModal_modal__price_button__']"
        ).filter(
                new Locator.FilterOptions().setHasText(expectedPrice)
        );
    }

    public void waitUntilVisible() {
        purchaseButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(30000)
        );
    }

    public void purchase() {
        purchaseButton.click();
    }
}