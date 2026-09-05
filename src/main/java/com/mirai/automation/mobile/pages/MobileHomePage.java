package com.mirai.automation.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MobileHomePage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By mobileMenuButton =
            By.cssSelector("button.xl\\:hidden");

    private final By loginButtons =
            By.xpath("//button[.//span[normalize-space()='Login']]");

    public MobileHomePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    public void acceptCookies() {
        WebElement button = wait.until(webDriver -> {
            Object result = ((JavascriptExecutor) webDriver).executeScript("""
                    const findElement = (root) => {
                        const acceptButton = root.querySelector?.('#accept');

                        if (acceptButton) {
                            return acceptButton;
                        }

                        const elements = root.querySelectorAll
                            ? root.querySelectorAll('*')
                            : [];

                        for (const element of elements) {
                            if (element.shadowRoot) {
                                const found = findElement(element.shadowRoot);

                                if (found) {
                                    return found;
                                }
                            }
                        }

                        return null;
                    };

                    return findElement(document);
                    """);

            return result instanceof WebElement
                    ? (WebElement) result
                    : null;
        });

        wait.until(
                ExpectedConditions.elementToBeClickable(button)
        );

        button.click();
    }

    public void openMobileMenu() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(mobileMenuButton)
        );

        button.click();
    }

    public void openLogin() {
        WebElement visibleLoginButton = wait.until(webDriver ->
                webDriver.findElements(loginButtons)
                        .stream()
                        .filter(WebElement::isDisplayed)
                        .filter(WebElement::isEnabled)
                        .findFirst()
                        .orElse(null)
        );

        visibleLoginButton.click();
    }
}