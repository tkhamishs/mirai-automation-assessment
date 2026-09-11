package com.mirai.automation.review;

import com.microsoft.playwright.Page;

public class TestContext {

    private static final ThreadLocal<Page> CURRENT_PAGE =
            new ThreadLocal<>();

    public static void setPage(Page page) {
        CURRENT_PAGE.set(page);
    }

    public static Page getPage() {
        return CURRENT_PAGE.get();
    }

    public static void clear() {
        CURRENT_PAGE.remove();
    }

    private TestContext() {
    }
}