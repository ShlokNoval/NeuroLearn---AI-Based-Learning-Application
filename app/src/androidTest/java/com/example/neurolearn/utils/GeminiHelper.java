package com.example.neurolearn.utils;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

public class GeminiHelper {

    private static final GenerativeModel model =
            new GenerativeModel(
                    "gemini-1.5-flash",
                    BuildConfig.GEMINI_API_KEY
            );

    public interface Callback {
        void onResponse(String text);
        void onError(String error);
    }

    public static void askQuestion(String prompt, Callback callback) {
        new Thread(() -> {
            try {
                GenerateContentResponse response = model.generateContent(prompt);
                callback.onResponse(response.getText());
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }
}