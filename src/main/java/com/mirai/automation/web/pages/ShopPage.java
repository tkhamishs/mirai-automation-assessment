package com.mirai.automation.web.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mirai.automation.config.Config;

public class ShopPage {

    private final Page page;
    private final Locator acceptCookiesButton;
    private final Locator productButtons;

    public ShopPage(Page page) {
        this.page = page;

        this.acceptCookiesButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Accept All")
        );

        this.productButtons = page.locator("button").filter(
                new Locator.FilterOptions().setHasText("SAR")
        );
    }

    public void open() {
        page.navigate(Config.BASE_URL + "shop");

        if (acceptCookiesButton.isVisible()) {
            acceptCookiesButton.click();
        }

        productButtons.first().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(30000)
        );
    }

    public int getAvailableProductCount() {
        return productButtons.count();
    }

    public ProductCard getFirstAvailableProduct() {
        return new ProductCard(productButtons.first());
    }
}