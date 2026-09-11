package com.mirai.automation.review;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReviewReportWriter {

    private static final Path OUTPUT_DIRECTORY =
            Path.of(
                    "target",
                    "qa-review"
            );

    public void write(
            ReviewReport report
    ) {

        try {
            Files.createDirectories(
                    OUTPUT_DIRECTORY
            );

            Path outputFile =
                    OUTPUT_DIRECTORY.resolve(
                            report.testName()
                                    + ".txt"
                    );

            String content =
                    """
                    QA REVIEW

                    Test Name: %s
                    Status: %s
                    Classification: %s
                    Reason: %s
                    Flaky Risk: %s
                    Next Action: %s
                    """
                            .formatted(
                                    report.testName(),
                                    report.status(),
                                    report.classification(),
                                    report.reason(),
                                    report.flakyRisk(),
                                    report.nextAction()
                            );

            Files.writeString(
                    outputFile,
                    content
            );

        } catch (IOException exception) {

            throw new RuntimeException(
                    "Failed to write QA review report",
                    exception
            );
        }
    }
}