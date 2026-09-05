package com.mirai.automation.web.pages;

import com.microsoft.playwright.Locator;

public class ProductCard {

    private final Locator root;

    public ProductCard(Locator root) {
        this.root = root;
    }

    public String getPrice() {
        return root.innerText().trim();
    }

    public void open() {
        root.click();
    }
}