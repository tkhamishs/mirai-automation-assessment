package com.mirai.automation.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MobileShopPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By productCards =
            By.cssSelector("div[class*='Card_card__']");

    public MobileShopPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    public int getAvailableProductCount() {
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(productCards)
        );

        return driver.findElements(productCards).size();
    }

    public MobileProductCard getFirstAvailableProduct() {
        List<WebElement> products = driver.findElements(productCards);

        WebElement firstVisibleProduct = products.stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No visible product was found in the mobile shop"
                        )
                );

        return new MobileProductCard(firstVisibleProduct);
    }
}