package com.mirai.automation.web.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.mirai.automation.web.pages.LoginModal;
import com.mirai.automation.web.pages.ProductCard;
import com.mirai.automation.web.pages.ShopPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PurchaseTest {

    @Test
    public void shouldPromptForLoginWhenStartingPurchase() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium()
                     .launch(new BrowserType.LaunchOptions().setHeadless(false))) {

            Page page = browser.newPage();

            ShopPage shopPage = new ShopPage(page);
            shopPage.open();

            Assert.assertTrue(
                    shopPage.getAvailableProductCount() > 0,
                    "No purchasable products were found in the shop"
            );

            ProductCard product = shopPage.getFirstAvailableProduct();

            String productPrice = product.getPrice();

            Assert.assertTrue(
                    productPrice.contains("SAR"),
                    "Selected product does not have a valid SAR price"
            );

            product.open();

            LoginModal loginModal = new LoginModal(page);

            Assert.assertTrue(
                    loginModal.isVisible(),
                    "Login modal was not displayed when starting the purchase flow"
            );
        }
    }
}