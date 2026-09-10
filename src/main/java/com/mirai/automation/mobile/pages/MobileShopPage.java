package com.mirai.automation.mobile.pages;

import com.mirai.automation.config.Config;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MobileShopPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By productCards =
            By.cssSelector(
                    "div[class*='Card_card__']"
            );

    private final By priceButton =
            By.cssSelector(
                    "button[class*='Card_card__price_button__']"
            );

    public MobileShopPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Config.DEFAULT_TIMEOUT
        );
    }

    public int getAvailableProductCount() {
        List<WebElement> availableProducts =
                wait.until(webDriver -> {

                    List<WebElement> products =
                            driver.findElements(
                                    productCards
                            );

                    List<WebElement> purchasableProducts =
                            products.stream()
                                    .filter(
                                            WebElement::isDisplayed
                                    )
                                    .filter(
                                            this::hasVisiblePriceButton
                                    )
                                    .toList();

                    return purchasableProducts.isEmpty()
                            ? null
                            : purchasableProducts;
                });

        return availableProducts.size();
    }

    public MobileProductCard getFirstAvailableProduct() {
        WebElement firstPurchasableProduct =
                wait.until(webDriver -> {

                    List<WebElement> products =
                            driver.findElements(
                                    productCards
                            );

                    return products.stream()
                            .filter(
                                    WebElement::isDisplayed
                            )
                            .filter(
                                    this::hasVisiblePriceButton
                            )
                            .findFirst()
                            .orElse(
                                    null
                            );
                });

        return new MobileProductCard(
                firstPurchasableProduct
        );
    }

    private boolean hasVisiblePriceButton(
            WebElement product
    ) {
        return product.findElements(
                        priceButton
                )
                .stream()
                .anyMatch(
                        WebElement::isDisplayed
                );
    }
}