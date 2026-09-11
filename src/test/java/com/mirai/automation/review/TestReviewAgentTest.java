package com.mirai.automation.review;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestReviewAgentTest {

    @Test
    public void shouldClassifyTimeoutFailure() {

        TestReviewAgent agent =
                new TestReviewAgent();

        ReviewReport report =
                agent.review(
                        "shouldCompleteMobilePurchaseFlowUntilPaymentConfirmation",
                        "FAIL",
                        "Timeout waiting for iframe Secure payment input frame"
                );

        System.out.println(
                "Test Name: " + report.testName()
        );

        System.out.println(
                "Status: " + report.status()
        );

        System.out.println(
                "Classification: " + report.classification()
        );

        System.out.println(
                "Reason: " + report.reason()
        );

        System.out.println(
                "Flaky Risk: " + report.flakyRisk()
        );

        System.out.println(
                "Next Action: " + report.nextAction()
        );

        Assert.assertEquals(
                report.classification(),
                "Potential flaky issue"
        );
    }
}