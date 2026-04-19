package com.example.neurolearn.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.neurolearn.R;
import com.example.neurolearn.utils.GroqHelper;

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

        GroqHelper.askQuestion(question, new GroqHelper.Callback() {
            @Override
            public void onResponse(String text) {
                requireActivity().runOnUiThread(() -> {
                    tvChat.append("\nAI: " + text);
                    scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                });
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