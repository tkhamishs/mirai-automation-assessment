package com.mirai.automation.mobile.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MobileProductCard {

    private final WebElement root;

    private final By priceButton =
            By.xpath(
                    ".//button[.//span[contains(@class,'uppercase')]]"
            );

    public MobileProductCard(WebElement root) {
        this.root = root;
    }

    public String getPrice() {
        return root.findElement(priceButton)
                .getText()
                .trim();
    }

    public void open() {
        root.click();
    }
}