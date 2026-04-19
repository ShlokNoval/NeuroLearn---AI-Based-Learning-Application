package com.example.neurolearn.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.example.neurolearn.R;
import com.example.neurolearn.utils.GeminiHelper;

public class ChatFragment extends Fragment {

    TextView tvChat;
    EditText etMessage;
    Button btnSend;
    ScrollView scrollView;

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        tvChat = view.findViewById(R.id.tvChat);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        scrollView = view.findViewById(R.id.scrollView);

        btnSend.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {
        String question = etMessage.getText().toString().trim();

        if (question.isEmpty()) return;

        tvChat.append("\n\nYou: " + question);

        GeminiHelper.askQuestion(question, new GeminiHelper.Callback() {
            @Override
            public void onResponse(String text) {
                requireActivity().runOnUiThread(() ->
                        tvChat.append("\nAI: " + text);

                scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
            }

            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() ->
                        tvChat.append("\nError: " + error)
                );
            }
        });

        etMessage.setText("");
    }
}