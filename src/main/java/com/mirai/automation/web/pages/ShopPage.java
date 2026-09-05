package com.mirai.automation.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mirai.automation.config.Config;

public class ShopPage {

    private final Page page;
    private final Locator acceptCookiesButton;
    private final Locator productCards;

    public ShopPage(Page page) {
        this.page = page;

        this.acceptCookiesButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Accept All")
        );

        this.productCards = page.locator(
                "div[class*='Card_card__']"
        );
    }

    public void open() {
        page.navigate(Config.BASE_URL + "shop");

        if (acceptCookiesButton.isVisible()) {
            acceptCookiesButton.click();
        }

        productCards.first().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(Config.DEFAULT_TIMEOUT.toMillis())
        );
    }

    public int getAvailableProductCount() {
        return productCards.count();
    }

    public ProductCard getFirstAvailableProduct() {
        return new ProductCard(productCards.first());
    }
}