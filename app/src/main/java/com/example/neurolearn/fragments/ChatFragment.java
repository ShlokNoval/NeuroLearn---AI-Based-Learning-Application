package com.example.neurolearn.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.neurolearn.R;
import com.example.neurolearn.adapters.ChatAdapter;
import com.example.neurolearn.models.ChatMessage;
import com.example.neurolearn.utils.GroqHelper;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText inputMessage;
    private Button btnSend;

    private List<ChatMessage> messages;
    private ChatAdapter adapter;

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        inputMessage = view.findViewById(R.id.inputMessage);
        btnSend = view.findViewById(R.id.btnSend);

        // Initialize message list
        messages = new ArrayList<>();

        // Setup adapter
        adapter = new ChatAdapter(messages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Send button click
        btnSend.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {

        String userText = inputMessage.getText().toString().trim();

        if (userText.isEmpty()) return;

        // Add user message
        messages.add(new ChatMessage(userText, true));
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messages.size() - 1);

        inputMessage.setText("");

        // 👉 STEP 1: Add "Typing..." message
        ChatMessage typingMessage = new ChatMessage("Typing...", false);
        messages.add(typingMessage);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messages.size() - 1);

        // 👉 STEP 2: Call AI
        GroqHelper.askQuestion(userText, new GroqHelper.Callback() {
            @Override
            public void onResponse(String response) {
                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> {

                    // Remove "Typing..."
                    messages.remove(typingMessage);

                    // Add real AI response
                    messages.add(new ChatMessage(response, false));

                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size() - 1);
                });
            }

            @Override
            public void onError(String error) {
                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> {

                    messages.remove(typingMessage);
                    messages.add(new ChatMessage("Error: " + error, false));

                    adapter.notifyDataSetChanged();
                });
            }
        });
    }
}