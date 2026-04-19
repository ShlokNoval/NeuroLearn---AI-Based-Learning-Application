package com.example.neurolearn.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.neurolearn.R;
import com.example.neurolearn.utils.GroqHelper;

public class QuizFragment extends Fragment {

    private EditText inputText;
    private Button btnGenerate;
    private TextView outputQuiz;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        inputText = view.findViewById(R.id.inputText);
        btnGenerate = view.findViewById(R.id.btnGenerate);
        outputQuiz = view.findViewById(R.id.outputQuiz);

        btnGenerate.setOnClickListener(v -> generateQuiz());
    }

    private void generateQuiz() {

        String userInput = inputText.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(userInput)) {
            outputQuiz.setText("⚠️ Please enter topic or notes");
            return;
        }

        // Show loading
        outputQuiz.setText("⏳ Generating quiz...");

        // Optimized prompt
        String prompt = "You are an exam generator.\n" +
                "Create 5 multiple choice questions (MCQs) from the content below.\n" +
                "Each question must have 4 options (A, B, C, D) and mention the correct answer.\n\n" +
                "Format strictly like this:\n" +
                "Q1. Question\n" +
                "A) Option\nB) Option\nC) Option\nD) Option\n" +
                "Answer: <correct option>\n\n" +
                "Content:\n" + userInput;

        // API Call
        GroqHelper.askQuestion(prompt, new GroqHelper.Callback() {
            @Override
            public void onResponse(String response) {
                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> {
                    outputQuiz.setText(response);
                });
            }

            @Override
            public void onError(String error) {
                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> {
                    outputQuiz.setText("❌ Error: " + error);
                });
            }
        });
    }
}