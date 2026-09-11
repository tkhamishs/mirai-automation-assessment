package com.mirai.automation.review;

import com.microsoft.playwright.Page;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class TestReviewListener implements ITestListener {

    private final TestReviewAgent reviewAgent =
            new TestReviewAgent();

    private final ReviewReportWriter reportWriter =
            new ReviewReportWriter();

    @Override
    public void onTestFailure(
            ITestResult result
    ) {

        String testName =
                result.getMethod()
                        .getMethodName();

        captureFailureScreenshot(
                testName
        );

        Throwable throwable =
                result.getThrowable();

        String failureMessage =
                throwable != null
                        ? throwable.toString()
                        : "No failure message available";

        ReviewReport report =
                reviewAgent.review(
                        testName,
                        "FAIL",
                        failureMessage
                );

        printReport(
                report
        );

        reportWriter.write(
                report
        );
    }

    private void captureFailureScreenshot(
            String testName
    ) {

        if (captureDesktopScreenshot(testName)) {
            return;
        }

        captureMobileScreenshot(
                testName
        );
    }

    private boolean captureDesktopScreenshot(
            String testName
    ) {

        Page page =
                TestContext.getPage();

        if (page == null) {
            return false;
        }

        try {

            Path screenshotDirectory =
                    createScreenshotDirectory();

            Path screenshotPath =
                    screenshotDirectory.resolve(
                            testName
                                    + "-web.png"
                    );

            page.screenshot(
                    new Page.ScreenshotOptions()
                            .setPath(
                                    screenshotPath
                            )
                            .setFullPage(
                                    true
                            )
            );

            System.out.println(
                    "Failure screenshot: "
                            + screenshotPath
            );

            return true;

        } catch (Exception exception) {

            System.out.println(
                    "Unable to capture desktop failure screenshot: "
                            + exception.getMessage()
            );

            return false;
        }
    }

    private void captureMobileScreenshot(
            String testName
    ) {

        AndroidDriver driver =
                MobileTestContext.getDriver();

        if (driver == null) {
            return;
        }

        try {

            Path screenshotDirectory =
                    createScreenshotDirectory();

            Path screenshotPath =
                    screenshotDirectory.resolve(
                            testName
                                    + "-mobile.png"
                    );

            File screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(
                                    OutputType.FILE
                            );

            Files.copy(
                    screenshot.toPath(),
                    screenshotPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println(
                    "Failure screenshot: "
                            + screenshotPath
            );

        } catch (Exception exception) {

            System.out.println(
                    "Unable to capture mobile failure screenshot: "
                            + exception.getMessage()
            );
        }
    }

    private Path createScreenshotDirectory()
            throws Exception {

        Path screenshotDirectory =
                Path.of(
                        "target",
                        "qa-review",
                        "screenshots"
                );

        Files.createDirectories(
                screenshotDirectory
        );

        return screenshotDirectory;
    }

    private void printReport(
            ReviewReport report
    ) {

        System.out.println();

        System.out.println(
                "========== QA REVIEW =========="
        );

        System.out.println(
                "Test Name: "
                        + report.testName()
        );

        System.out.println(
                "Status: "
                        + report.status()
        );

        System.out.println(
                "Classification: "
                        + report.classification()
        );

        System.out.println(
                "Reason: "
                        + report.reason()
        );

        System.out.println(
                "Flaky Risk: "
                        + report.flakyRisk()
        );

        System.out.println(
                "Next Action: "
                        + report.nextAction()
        );

        System.out.println(
                "==============================="
        );

        System.out.println();
    }
}