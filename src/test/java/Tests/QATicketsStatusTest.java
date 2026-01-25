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

    // ✅ Get secrets from GitLab CI/CD variables
    static String GITLAB_TOKEN = System.getenv("GITLAB_TOKEN");
    static String GCHAT_WEBHOOK_URL = System.getenv("GCHAT_WEBHOOK_URL");

    static String PROJECT_ID = "24465831";
    static String LABEL_NAME = "QA";

    @Test
    public void sendQABucketStatusToGChat() {

        // ✅ Safety check
        if (GITLAB_TOKEN == null || GCHAT_WEBHOOK_URL == null) {
            throw new RuntimeException("❌ Missing GitLab CI variables: GITLAB_TOKEN or GCHAT_WEBHOOK_URL");
        }

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
