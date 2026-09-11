package com.mirai.automation.review;

public record ReviewReport(
        String testName,
        String status,
        String classification,
        String reason,
        String flakyRisk,
        String nextAction
) {
}