package com.mcpserver.playwright;

import com.microsoft.playwright.options.ScreenshotScale;
import com.microsoft.playwright.options.ScreenshotType;
import org.springframework.ai.tool.annotation.Tool;
import com.microsoft.playwright.*;
import org.springframework.stereotype.Service;
import jakarta.annotation.PreDestroy;

import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayWrightService {

    private Playwright playwright;
    private Browser browser;
    private Page page;
    
    // Initialize Playwright resources only when needed
    private void initializePlaywright() {
        if (playwright == null) {
            playwright = Playwright.create();
        }
        
        if (browser == null) {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }
        
        if (page == null) {
            page = browser.newPage();
        }
    }

    @Tool(description = "navigate to a URL")
    public String navigateToWebsite(String url) {
        initializePlaywright();
        page.navigate(url);
        return "Navigated to " + url;
    }

    @Tool(description = "Take a screenshot of the current page", name = "screenshot")
    public Map<String, Object> playwrightScreenshot(String path) {
        initializePlaywright();

        // Capture the screenshot asynchronously
        byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setType(ScreenshotType.JPEG).setQuality(50).setScale(ScreenshotScale.CSS));

        // Encode the screenshot to base64
        String base64Screenshot = Base64.getEncoder().encodeToString(screenshotBytes);

        // Return the base64-encoded screenshot as part of the content
        Map<String, Object> result = new HashMap<>();
        result.put("content", List.of(Map.of(
                "type", "image",
                "data", base64Screenshot,
                "mimeType", "image/jpeg"
        )));

        // Optionally, you can save the screenshot to disk if desired
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));

        return result;
    }

    @Tool(description = "click on an element specified by a selector")
    public String playwrightClick(String selector) {
        initializePlaywright();
        page.click(selector);
        return "Clicked on element with selector " + selector;
    }

    @Tool(description = "click on an element within an iframe specified by a selector")
    public String playwrightIframeClick(String iframeSelector, String elementSelector) {
        initializePlaywright();
        page.frame(iframeSelector).click(elementSelector);
        return "Clicked on element within iframe with selector " + elementSelector;
    }

    @Tool(description = "select an option from a dropdown menu specified by a selector")
    public String playwrightSelect(String selector, String value) {
        initializePlaywright();
        page.selectOption(selector, value);
        return "Selected value " + value + " from dropdown with selector " + selector;
    }

    @Tool(description = "fill a form field with text")
    public String playwrightFill(String selector, String value) {
        initializePlaywright();
        page.fill(selector, value);
        return "Filled " + selector + " with value: " + value;
    }

    @Tool(description = "hover over an element")
    public String playwrightHover(String selector) {
        initializePlaywright();
        page.hover(selector);
        return "Hovered over element with selector: " + selector;
    }

    @Tool(description = "evaluate JavaScript code on the page")
    public Object playwrightEvaluate(String javascript) {
        initializePlaywright();
        return page.evaluate(javascript);
    }

    @Tool(description = "get console logs from the page")
    public String playwrightConsoleLogs() {
        initializePlaywright();
        StringBuilder logs = new StringBuilder();
        page.onConsoleMessage(message -> {
            logs.append(message.type()).append(": ").append(message.text()).append("\n");
        });
        return "Console logs captured: " + logs.toString();
    }

    @Tool(description = "close the current page")
    public String playwrightClosePage() {
        if (page != null) {
            page.close();
            page = null;
            return "Page closed successfully";
        }
        return "No page to close";
    }

    @PreDestroy
    public void close() {
        if (page != null) {
            page.close();
            page = null;
        }
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }
}
