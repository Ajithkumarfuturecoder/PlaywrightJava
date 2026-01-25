package Tests;

import java.time.LocalTime;
import java.util.Map;

import org.json.JSONArray;
import org.testng.annotations.Test;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class QATicketsStatusTest {

    static String GITLAB_TOKEN = "glpat-WzTl0uVz8ytUd7aGgMkV3m86MQp1OmgwMDFpCw.01.121rnlkwk";
    static String GCHAT_WEBHOOK_URL = "https://chat.googleapis.com/v1/spaces/AAQAiGKsr7s/messages?key=AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI&token=E3eVaauHujI9m12X0Jr30Ibn4jHM7BTw4afTpYA-qyQ";

    static String PROJECT_ID = "24465831";
    static String LABEL_NAME = "QA";

    @Test
    public void sendQABucketStatusToGChat() {

        String gitlabIssuesUrl = "https://gitlab.com/api/v4/projects/" + PROJECT_ID +
                "/issues?state=opened&labels=" + LABEL_NAME + "&per_page=100";

        try (Playwright playwright = Playwright.create()) {

            APIRequestContext api = playwright.request().newContext(
                    new APIRequest.NewContextOptions().setExtraHTTPHeaders(
                            Map.of("PRIVATE-TOKEN", GITLAB_TOKEN)
                    )
            );

            APIResponse response = api.get(gitlabIssuesUrl);
            String responseBody = response.text();

            JSONArray issues = new JSONArray(responseBody);
            int qaCount = issues.length();

            String tag = LocalTime.now().getHour() < 12 ? "🌅 Morning" : "🌙 Evening";

            // ✅ Java 8 compatible JSON
            String gchatMessage = "{\"text\":\"" + tag +
                    " QA Bucket Status\\n🔖 Open issues with label QA: " + qaCount + "\"}";

            APIResponse gchatRes = api.post(GCHAT_WEBHOOK_URL,
                    RequestOptions.create()
                            .setHeader("Content-Type", "application/json")
                            .setData(gchatMessage)
            );

            System.out.println("✅ Sent QA bucket count to GChat: " + qaCount);
            System.out.println("✅ GChat Response Code: " + gchatRes.status());
        }
    }
}
