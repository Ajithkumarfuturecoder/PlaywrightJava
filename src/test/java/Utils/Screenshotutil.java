package Utils;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.microsoft.playwright.Page;

public class Screenshotutil {

    public static String takeScreenshot(Page page, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = "test-output/screenshots/" + testName + "_" + timestamp + ".jpeg";

        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));

        return path;
    }
}
