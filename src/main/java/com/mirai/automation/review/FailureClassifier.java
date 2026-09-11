package com.mirai.automation.review;

public class FailureClassifier {

    public String classify(String failureMessage) {

        if (failureMessage == null || failureMessage.isBlank()) {
            return "Needs investigation";
        }

        String message =
                failureMessage.toLowerCase();

        if (message.contains("timeout")
                || message.contains("stale element")
                || message.contains("element not interactable")) {

            return "Potential flaky issue";
        }

        if (message.contains("connection refused")
                || message.contains("session not created")
                || message.contains("chromedriver")
                || message.contains("device")
                || message.contains("network")) {

            return "Environment issue";
        }

        if (message.contains("assert")
                || message.contains("expected")
                || message.contains("actual")) {

            return "Product issue";
        }

        if (message.contains("locator")
                || message.contains("selector")
                || message.contains("no such element")) {

            return "Automation issue";
        }

        return "Needs investigation";
    }
}