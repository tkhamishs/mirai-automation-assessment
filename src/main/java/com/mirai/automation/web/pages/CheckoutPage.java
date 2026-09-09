package com.mirai.automation.web.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.mirai.automation.config.Config;

public class CheckoutPage {

    private final FrameLocator paymentFrame;
    private final FrameLocator cardFrame;
    private final FrameLocator emailFrame;

    private final Locator paymentTypeTitle;
    private final Locator cardPaymentOption;

    private final Locator cardNumberField;
    private final Locator expiryField;
    private final Locator cvcField;
    private final Locator emailField;

    private final Locator subtotalPrice;
    private final Locator totalPrice;
    private final Locator payButton;

    public CheckoutPage(Page page) {

        this.paymentFrame = page.frameLocator(
                "iframe.payment-container-iframe"
        );

        this.cardFrame = paymentFrame.frameLocator(
                "iframe[title='Secure payment input frame']"
        );

        this.emailFrame = paymentFrame.frameLocator(
                "iframe[src*='secure-components/text-input/email']"
        );

        this.paymentTypeTitle = paymentFrame.getByText(
                "Payment type"
        );

        this.cardPaymentOption = paymentFrame.locator(
                "input[type='radio'][value='creditcard']"
        );

        this.cardNumberField = cardFrame.locator(
                "#payment-numberInput"
        );

        this.expiryField = cardFrame.locator(
                "#payment-expiryInput"
        );

        this.cvcField = cardFrame.locator(
                "#payment-cvcInput"
        );

        this.emailField = emailFrame.locator(
                "#x-text-input-input-email"
        );

        this.subtotalPrice = paymentFrame.locator(
                ".subtotal-row psdk-price-text"
        );

        this.totalPrice = paymentFrame.locator(
                "psdk-total psdk-price-text"
        );

        this.payButton = paymentFrame.getByRole(
                AriaRole.BUTTON,
                new FrameLocator.GetByRoleOptions()
                        .setName("Pay")
                        .setExact(true)
        );
    }

    public void waitUntilVisible() {
        paymentTypeTitle.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(Config.CHECKOUT_TIMEOUT.toMillis())
        );

        cardNumberField.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(Config.CHECKOUT_TIMEOUT.toMillis())
        );
    }

    public boolean isPaymentTypeVisible() {
        return paymentTypeTitle.isVisible();
    }

    public boolean isCardPaymentOptionSelected() {
        return cardPaymentOption.isChecked();
    }

    public boolean isCardNumberFieldVisible() {
        return cardNumberField.isVisible();
    }

    public boolean isExpiryFieldVisible() {
        return expiryField.isVisible();
    }

    public boolean isCvcFieldVisible() {
        return cvcField.isVisible();
    }

    public boolean isEmailFieldVisible() {
        return emailField.isVisible();
    }

    public String getSubtotalAmount() {
        return subtotalPrice.getAttribute(
                "price-line-amount"
        );
    }

    public String getSubtotalCurrency() {
        return subtotalPrice.getAttribute(
                "price-line-currency"
        );
    }

    public String getTotalAmount() {
        return totalPrice.getAttribute(
                "amount"
        );
    }

    public String getTotalCurrency() {
        return totalPrice.getAttribute(
                "currency"
        );
    }

    public boolean isPayButtonVisible() {
        return payButton.isVisible();
    }
}