package com.example.neurolearn.utils;

import android.os.Handler;
import android.os.Looper;

import com.example.neurolearn.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroqHelper {

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL   = "llama-3.1-8b-instant";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public interface Callback {
        void onResponse(String text);
        void onError(String error);
    }

    public static void askQuestion(String prompt, Callback callback) {
        new Thread(() -> {
            try {
                // Build JSON body
                JSONObject message = new JSONObject();
                message.put("role", "user");
                message.put("content", prompt);

                JSONArray messages = new JSONArray();
                messages.put(message);

                JSONObject body = new JSONObject();
                body.put("model", MODEL);
                body.put("messages", messages);
                body.put("max_tokens", 1024);

                RequestBody requestBody = RequestBody.create(
                        body.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(API_URL)
                        .addHeader("Authorization", "Bearer " + BuildConfig.GROQ_API_KEY)
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    postError(callback, "HTTP " + response.code() + ": " + responseBody);
                    return;
                }

                // Parse response
                JSONObject json   = new JSONObject(responseBody);
                String result     = json.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");

                if (result == null || result.trim().isEmpty()) {
                    result = "No response from AI";
                }

                postSuccess(callback, result.trim());

            } catch (Exception e) {
                postError(callback, e.toString());
            }
        }).start();
    }

    private static void postSuccess(Callback callback, String result) {
        new Handler(Looper.getMainLooper()).post(() ->
                callback.onResponse(result)
        );
    }

    private static void postError(Callback callback, String error) {
        new Handler(Looper.getMainLooper()).post(() ->
                callback.onError(error)
        );
    }
}