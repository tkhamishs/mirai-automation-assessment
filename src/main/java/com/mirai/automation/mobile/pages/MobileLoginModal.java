package com.mirai.automation.mobile.pages;

import com.mirai.automation.config.Config;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MobileLoginModal {

    private final WebDriverWait wait;

    private final By continueWithEmailButton =
            By.cssSelector("button img[alt='Email logo']");

    public MobileLoginModal(AndroidDriver driver) {
        this.wait = new WebDriverWait(
                driver,
                Config.DEFAULT_TIMEOUT
        );
    }

    public boolean isVisible() {
        WebElement emailLogo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        continueWithEmailButton
                )
        );

        return emailLogo.isDisplayed();
    }

    public void continueWithEmail() {
        WebElement emailLogo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        continueWithEmailButton
                )
        );

        WebElement button = emailLogo.findElement(
                By.xpath("./ancestor::button")
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(button)
        );

        button.click();
    }
}