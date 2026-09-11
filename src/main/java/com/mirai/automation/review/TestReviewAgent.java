package com.mirai.automation.review;

public class TestReviewAgent {

    private final FailureClassifier failureClassifier;

    public TestReviewAgent() {
        this.failureClassifier =
                new FailureClassifier();
    }

    public ReviewReport review(
            String testName,
            String status,
            String failureMessage
    ) {

        if ("PASS".equalsIgnoreCase(status)) {
            return new ReviewReport(
                    testName,
                    "PASS",
                    "No issue",
                    "The test completed successfully.",
                    "Low",
                    "No action required."
            );
        }

        String classification =
                failureClassifier.classify(
                        failureMessage
                );

        return switch (classification) {

            case "Potential flaky issue" ->
                    new ReviewReport(
                            testName,
                            status,
                            classification,
                            "The failure contains signs of a timing, stale element, or synchronization problem.",
                            "High",
                            "Check synchronization, element re-rendering, and whether the failure reproduces consistently."
                    );

            case "Environment issue" ->
                    new ReviewReport(
                            testName,
                            status,
                            classification,
                            "The failure contains signs of an environment, driver, device, or connectivity problem.",
                            "Medium",
                            "Check the test environment, browser or driver compatibility, Appium session, and network availability."
                    );

            case "Product issue" ->
                    new ReviewReport(
                            testName,
                            status,
                            classification,
                            "The observed result appears different from the expected test result.",
                            "Low",
                            "Reproduce the scenario manually and confirm whether the application behavior is incorrect."
                    );

            case "Automation issue" ->
                    new ReviewReport(
                            testName,
                            status,
                            classification,
                            "The failure contains signs that the automation could not locate or interact with the expected element.",
                            "Medium",
                            "Review the locator, page state, iframe context, and element availability."
                    );

            default ->
                    new ReviewReport(
                            testName,
                            status,
                            "Needs investigation",
                            "There is not enough evidence to classify the failure confidently.",
                            "Unknown",
                            "Review the stack trace, test logs, page state, and reproduce the failure before assigning a category."
                    );
        };
    }
}