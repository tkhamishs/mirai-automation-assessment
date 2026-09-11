package com.mirai.automation.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mirai.automation.config.Config;

public class ProductCard {

    private final Locator root;
    private final Locator priceButton;

    public ProductCard(Locator root) {

        this.root =
                root;

        this.priceButton =
                root.locator(
                        "button[class*='Card_card__price_button__']"
                ).first();
    }

    public String getPrice() {

        priceButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(
                                WaitForSelectorState.VISIBLE
                        )
                        .setTimeout(
                                Config.DEFAULT_TIMEOUT.toMillis()
                        )
        );

        return priceButton
                .innerText()
                .trim();
    }

    public void open() {

        root.click();
    }
}