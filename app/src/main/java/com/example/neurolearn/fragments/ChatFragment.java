package com.example.neurolearn.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.neurolearn.R;
import com.example.neurolearn.adapters.ChatAdapter;
import com.example.neurolearn.models.ChatMessage;
import com.example.neurolearn.utils.GroqHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText inputMessage;
    private Button btnSend, btnMic;

    private List<ChatMessage> messages;
    private ChatAdapter adapter;

    private static final int REQUEST_SPEECH = 100;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        inputMessage = view.findViewById(R.id.inputMessage);
        btnSend = view.findViewById(R.id.btnSend);
        btnMic = view.findViewById(R.id.btnMic);

        messages = new ArrayList<>();
        adapter = new ChatAdapter(messages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendMessage());
        btnMic.setOnClickListener(v -> startVoiceInput());

        return view;
    }

    private void sendMessage() {
        String userText = inputMessage.getText().toString().trim();

        if (userText.isEmpty()) {
            return;
        }

        messages.add(new ChatMessage(userText, true));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);

        inputMessage.setText("");

        ChatMessage typingMessage = new ChatMessage("Typing...", false);
        messages.add(typingMessage);
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);

        GroqHelper.askQuestion(userText, new GroqHelper.Callback() {
            @Override
            public void onResponse(String response) {
                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> {
                    int typingIndex = messages.indexOf(typingMessage);
                    if (typingIndex != -1) {
                        messages.remove(typingIndex);
                        adapter.notifyItemRemoved(typingIndex);
                    }

                    messages.add(new ChatMessage(response, false));
                    adapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);
                });
            }

            @Override
            public void onError(String error) {
                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> {
                    int typingIndex = messages.indexOf(typingMessage);
                    if (typingIndex != -1) {
                        messages.remove(typingIndex);
                        adapter.notifyItemRemoved(typingIndex);
                    }

                    messages.add(new ChatMessage("Error: " + error, false));
                    adapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);
                });
            }
        });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your question...");

        try {
            startActivityForResult(intent, REQUEST_SPEECH);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Voice not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (result != null && !result.isEmpty()) {
                inputMessage.setText(result.get(0));
                inputMessage.setSelection(inputMessage.getText().length());
            }
        }
    }
}