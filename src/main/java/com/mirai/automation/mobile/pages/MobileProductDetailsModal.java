package com.mirai.automation.mobile.pages;

import com.mirai.automation.config.Config;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MobileProductDetailsModal {

    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private final String expectedPrice;

    public MobileProductDetailsModal(
            AndroidDriver driver,
            String expectedPrice
    ) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Config.DEFAULT_TIMEOUT
        );
        this.expectedPrice = expectedPrice;
    }

    private WebElement getPurchaseButton() {
        return wait.until(webDriver -> {
            List<WebElement> buttons =
                    driver.findElements(By.tagName("button"));

            return buttons.stream()
                    .filter(WebElement::isDisplayed)
                    .filter(WebElement::isEnabled)
                    .filter(button ->
                            button.getText()
                                    .trim()
                                    .equals(expectedPrice)
                    )
                    .reduce((first, second) -> second)
                    .orElse(null);
        });
    }

    public void waitUntilVisible() {
        getPurchaseButton();
    }

    public void purchase() {
        getPurchaseButton().click();
    }
}