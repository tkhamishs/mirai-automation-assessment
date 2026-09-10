package com.mirai.automation.mobile.pages;

import com.mirai.automation.config.Config;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MobileCheckoutPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    private final By paymentFrame =
            By.cssSelector(
                    "iframe.payment-container-iframe"
            );

    private final By cardPaymentOption =
            By.cssSelector(
                    "input[type='radio'][value='creditcard']"
            );

    private final By payWithCardHeading =
            By.xpath(
                    "//h1[normalize-space()='Pay with Card']"
            );

    private final By stripeCardFrame =
            By.cssSelector(
                    "iframe[title='Secure payment input frame']"
            );

    private final By emailFrame =
            By.cssSelector(
                    "iframe[src*='secure-components/text-input/email']"
            );

    private final By cardNumberField =
            By.id("payment-numberInput");

    private final By expiryField =
            By.id("payment-expiryInput");

    private final By cvcField =
            By.id("payment-cvcInput");

    private final By emailField =
            By.id("x-text-input-input-email");

    private final By subtotalPrice =
            By.cssSelector(
                    ".subtotal-row psdk-price-text"
            );

    private final By totalPrice =
            By.cssSelector(
                    "psdk-total psdk-price-text"
            );

    private final By payButton =
            By.xpath(
                    "//button[@aria-label='Pay']"
            );

    public MobileCheckoutPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Config.CHECKOUT_TIMEOUT
        );
    }

    private void switchToPaymentFrame() {
        driver.switchTo().defaultContent();

        WebElement frame = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        paymentFrame
                )
        );

        driver.switchTo().frame(
                frame
        );
    }

    private void switchToStripeCardFrame() {
        switchToPaymentFrame();

        WebElement stripeFrame = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        stripeCardFrame
                )
        );

        driver.switchTo().frame(
                stripeFrame
        );
    }

    private void switchToEmailFrame() {
        switchToPaymentFrame();

        WebElement frame = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        emailFrame
                )
        );

        driver.switchTo().frame(
                frame
        );
    }

    public void selectCardPayment() {
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            switchToPaymentFrame();

            WebElement cardOption = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            cardPaymentOption
                    )
            );

            WebElement cardLabel =
                    cardOption.findElement(
                            By.xpath("./ancestor::label")
                    );

            ((JavascriptExecutor) driver)
                    .executeScript(
                            "arguments[0].scrollIntoView({block:'center'});",
                            cardLabel
                    );

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            cardLabel
                    )
            );

            cardLabel.click();

            switchToPaymentFrame();

            WebDriverWait cardFormWait =
                    new WebDriverWait(
                            driver,
                            Duration.ofSeconds(10)
                    );

            try {
                cardFormWait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                stripeCardFrame
                        )
                );

                return;

            } catch (TimeoutException exception) {

                System.out.println(
                        "Card payment did not load after attempt "
                                + attempt
                                + ". Retrying..."
                );
            }
        }

        throw new TimeoutException(
                "Card payment form did not load after 3 attempts"
        );
    }

    public boolean isPayWithCardVisible() {
        switchToPaymentFrame();

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        payWithCardHeading
                )
        ).isDisplayed();
    }

    public boolean isCardPaymentFormVisible() {
        switchToPaymentFrame();

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        stripeCardFrame
                )
        ).isDisplayed();
    }

    public boolean isCardNumberFieldVisible() {
        switchToStripeCardFrame();

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        cardNumberField
                )
        ).isDisplayed();
    }

    public boolean isExpiryFieldVisible() {
        switchToStripeCardFrame();

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        expiryField
                )
        ).isDisplayed();
    }

    public boolean isCvcFieldVisible() {
        switchToStripeCardFrame();

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        cvcField
                )
        ).isDisplayed();
    }

    public boolean isEmailFieldVisible() {
        switchToEmailFrame();

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        emailField
                )
        ).isDisplayed();
    }

    public String getSubtotalAmount() {
        switchToPaymentFrame();

        return wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        subtotalPrice
                )
        ).getAttribute(
                "price-line-amount"
        );
    }

    public String getSubtotalCurrency() {
        switchToPaymentFrame();

        return wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        subtotalPrice
                )
        ).getAttribute(
                "price-line-currency"
        );
    }

    public String getTotalAmount() {
        switchToPaymentFrame();

        return wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        totalPrice
                )
        ).getAttribute(
                "amount"
        );
    }

    public String getTotalCurrency() {
        switchToPaymentFrame();

        return wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        totalPrice
                )
        ).getAttribute(
                "currency"
        );
    }

    public boolean isPayButtonVisible() {
        switchToPaymentFrame();

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        payButton
                )
        ).isDisplayed();
    }
}