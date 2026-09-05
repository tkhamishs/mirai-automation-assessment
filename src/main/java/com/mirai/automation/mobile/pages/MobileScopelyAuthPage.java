package com.mirai.automation.mobile.pages;

import com.mirai.automation.config.Config;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MobileScopelyAuthPage {

    private final WebDriverWait wait;

    private final By pageHeading =
            By.xpath("//*[contains(text(), \"Let's get you in\")]");

    private final By emailField =
            By.cssSelector("input");

    private final By continueButton =
            By.xpath("//button[normalize-space()='Continue']");

    private final By verificationHeading =
            By.xpath("//*[contains(text(), 'Check your inbox!')]");

    private final By verificationMessage =
            By.xpath("//*[contains(text(), \"We've sent a code to\")]");

    public MobileScopelyAuthPage(AndroidDriver driver) {
        this.wait = new WebDriverWait(
                driver,
                Config.DEFAULT_TIMEOUT
        );
    }

    public void enterEmail(String email) {
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(pageHeading)
        );

        WebElement field = wait.until(
                ExpectedConditions.visibilityOfElementLocated(emailField)
        );

        field.sendKeys(email);
    }

    public String getEmailValue() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(emailField)
        ).getAttribute("value");
    }

    public void continueLogin() {
        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(continueButton)
        );

        button.click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(verificationHeading)
        );
    }

    public boolean isVerificationScreenVisible() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(verificationHeading)
        ).isDisplayed();
    }

    public String getVerificationMessage() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(verificationMessage)
        ).getText();
    }
}