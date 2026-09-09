package com.mirai.automation.web.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.mirai.automation.config.Config;
import com.mirai.automation.web.pages.CheckoutPage;
import com.mirai.automation.web.pages.ProductCard;
import com.mirai.automation.web.pages.ProductDetailsModal;
import com.mirai.automation.web.pages.ShopPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class PurchaseTest {

    @Test
    public void shouldCompletePurchaseFlowUntilPaymentConfirmation() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium()
                     .launch(
                             new BrowserType.LaunchOptions()
                                     .setHeadless(false)
                                     .setSlowMo(1200)
                     )) {

            BrowserContext context =
                    browser.newContext(
                            new Browser.NewContextOptions()
                                    .setStorageStatePath(
                                            Paths.get("auth-state.json")
                                    )
                    );

            Page page =
                    context.newPage();

            ShopPage shopPage =
                    new ShopPage(page);

            shopPage.open();

            Assert.assertTrue(
                    shopPage.getAvailableProductCount() > 0,
                    "No purchasable products were found in the shop"
            );

            PurchaseData productData =
                    getProductData(shopPage);

            ProductDetailsModal productDetailsModal =
                    new ProductDetailsModal(
                            page,
                            productData.displayPrice()
                    );

            productDetailsModal.waitUntilVisible();
            productDetailsModal.purchase();

            CheckoutPage checkoutPage =
                    new CheckoutPage(page);

            checkoutPage.waitUntilVisible();

            Assert.assertTrue(
                    checkoutPage.isPaymentTypeVisible(),
                    "Payment type section is not visible"
            );

            Assert.assertTrue(
                    checkoutPage.isCardPaymentOptionSelected(),
                    "Card payment option is not selected"
            );

            Assert.assertTrue(
                    checkoutPage.isCardNumberFieldVisible(),
                    "Card number field is not visible"
            );

            Assert.assertTrue(
                    checkoutPage.isExpiryFieldVisible(),
                    "Expiry field is not visible"
            );

            Assert.assertTrue(
                    checkoutPage.isCvcFieldVisible(),
                    "CVC field is not visible"
            );

            Assert.assertTrue(
                    checkoutPage.isEmailFieldVisible(),
                    "Receipt email field is not visible"
            );

            Assert.assertEquals(
                    checkoutPage.getSubtotalAmount(),
                    productData.amount(),
                    "Checkout subtotal amount does not match the selected product price"
            );

            Assert.assertEquals(
                    checkoutPage.getSubtotalCurrency(),
                    "SAR",
                    "Checkout subtotal currency is incorrect"
            );

            Assert.assertEquals(
                    checkoutPage.getTotalAmount(),
                    productData.amount(),
                    "Checkout total amount does not match the selected product price"
            );

            Assert.assertEquals(
                    checkoutPage.getTotalCurrency(),
                    "SAR",
                    "Checkout total currency is incorrect"
            );

            Assert.assertTrue(
                    checkoutPage.isPayButtonVisible(),
                    "Pay button is not visible"
            );

            // Recording-only hold so the validated checkout
            // remains visible before the browser closes.
            page.waitForTimeout(10000);
        }
    }

    private PurchaseData getProductData(
            ShopPage shopPage
    ) {

        ProductCard product =
                shopPage.getFirstAvailableProduct();

        String productPrice =
                product.getPrice();

        Assert.assertTrue(
                productPrice.contains("SAR"),
                "Selected product does not have a valid SAR price"
        );

        String expectedAmount =
                productPrice.replaceAll(
                        "[^0-9.,]",
                        ""
                );

        product.open();

        return new PurchaseData(
                productPrice,
                expectedAmount
        );
    }

    private record PurchaseData(
            String displayPrice,
            String amount
    ) {
    }
}