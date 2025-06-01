package snapmeal.snapmeal.global;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiClient {

    @Value("${API_KEY}")
    private String apiKey;

    private final String endpoint = "https://api.openai.com/v1/chat/completions";
    private final String model = "gpt-4o-mini"; // 또는 gpt-4o, gpt-3.5-turbo 등

    public String requestCompletion(String systemPrompt, String userPrompt) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject()
                .put("model", model)
                .put("messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ))
                .put("temperature", 0.7);

        Request request = new Request.Builder()
                .url(endpoint)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(json.toString(), MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            JSONObject result = new JSONObject(body);
            return result.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
        }
    }
}