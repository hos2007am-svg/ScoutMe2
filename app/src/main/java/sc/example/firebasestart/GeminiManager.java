package sc.example.firebasestart;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiManager {
    private static GeminiManager instance;
    private OkHttpClient client;
    private static final String API_KEY = BuildConfig.Gemini_API_Key ;
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    private GeminiManager() {
        client = new OkHttpClient();
    }

    public static GeminiManager getInstance() {
        if (instance == null) {
            instance = new GeminiManager();
        }
        return instance;
    }

    public void sentTextPrompt(String prompt, GeminiCallback callback) {
        try {
            JSONObject textPart = new JSONObject();
            textPart.put("text", prompt);

            JSONArray partsArray = new JSONArray();
            partsArray.put(textPart);

            JSONObject contentObject = new JSONObject();
            contentObject.put("parts", partsArray);

            JSONArray contentsArray = new JSONArray();
            contentsArray.put(contentObject);

            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", contentsArray);

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Log.d("HOSAM_ERROR", e.getMessage());
                    callback.onFailure(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();

                        JSONObject json = new JSONObject(responseBody);

                        JSONArray candidates = json.getJSONArray("candidates");
                        JSONObject candidate = candidates.getJSONObject(0);

                        JSONObject content = candidate.getJSONObject("content");
                        JSONArray parts = content.getJSONArray("parts");

                        String text = parts.getJSONObject(0).getString("text");

                        callback.onSuccess(text);

                    } catch (Exception e) {
                        Log.d("HOSAM_ERROR", e.getMessage());
                        callback.onFailure(e);
                    }
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}