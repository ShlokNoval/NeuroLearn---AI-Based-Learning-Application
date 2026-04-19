package com.example.neurolearn.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.neurolearn.R;
import com.example.neurolearn.adapters.QuizAdapter;
import com.example.neurolearn.models.QuizQuestion;
import com.example.neurolearn.utils.GroqHelper;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private EditText inputText;
    private Button btnGenerate, btnSubmit;
    private RecyclerView recyclerQuiz;

    private List<QuizQuestion> questionList = new ArrayList<>();
    private QuizAdapter adapter;

    public QuizFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputText = view.findViewById(R.id.inputText);
        btnGenerate = view.findViewById(R.id.btnGenerate);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        recyclerQuiz = view.findViewById(R.id.recyclerQuiz);

        // Setup RecyclerView
        adapter = new QuizAdapter(questionList);
        recyclerQuiz.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerQuiz.setAdapter(adapter);

        btnGenerate.setOnClickListener(v -> generateQuiz());
        btnSubmit.setOnClickListener(v -> calculateScore());
    }

    private void generateQuiz() {

        String userInput = inputText.getText().toString().trim();

        if (TextUtils.isEmpty(userInput)) {
            Toast.makeText(getContext(), "Enter topic first", Toast.LENGTH_SHORT).show();
            return;
        }

        String prompt = "Generate 5 MCQ questions from the following content.\n" +
                "Format strictly:\n" +
                "Q1. Question\nA) Option\nB) Option\nC) Option\nD) Option\nAnswer: A\n\n" +
                "Content:\n" + userInput;

        Toast.makeText(getContext(), "Generating quiz...", Toast.LENGTH_SHORT).show();

        GroqHelper.askQuestion(prompt, new GroqHelper.Callback() {
            @Override
            public void onResponse(String response) {

                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> parseQuiz(response));
            }

            @Override
            public void onError(String error) {
                if (getActivity() == null) return;

                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void parseQuiz(String text) {

        questionList.clear();
        adapter.getSelectedAnswers().clear(); // ✅ Clear old selections

        String[] blocks = text.split("Q");

        for (String block : blocks) {

            if (block.trim().isEmpty()) continue;

            try {
                String[] lines = block.trim().split("\n");

                if (lines.length < 6) continue; // safety check

                String question = lines[0].replaceAll("\\d+\\. ", "").trim();

                List<String> options = new ArrayList<>();
                options.add(lines[1].substring(3).trim());
                options.add(lines[2].substring(3).trim());
                options.add(lines[3].substring(3).trim());
                options.add(lines[4].substring(3).trim());

                // Extract first character (A/B/C/D safely)
                String answerLine = lines[5].split(":")[1].trim();
                String answer = answerLine.substring(0, 1);

                questionList.add(new QuizQuestion(question, options, answer));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        adapter.notifyDataSetChanged();

        Toast.makeText(getContext(), "Quiz Ready!", Toast.LENGTH_SHORT).show();
    }

    private void calculateScore() {

        int score = 0;

        for (int i = 0; i < questionList.size(); i++) {

            if (!adapter.getSelectedAnswers().containsKey(i)) continue;

            int selectedIndex = adapter.getSelectedAnswers().get(i);

            String selectedOption = "";
            if (selectedIndex == 0) selectedOption = "A";
            else if (selectedIndex == 1) selectedOption = "B";
            else if (selectedIndex == 2) selectedOption = "C";
            else if (selectedIndex == 3) selectedOption = "D";

            if (selectedOption.equalsIgnoreCase(questionList.get(i).answer)) {
                score++;
            }
        }

        Toast.makeText(getContext(),
                "Score: " + score + "/" + questionList.size(),
                Toast.LENGTH_LONG).show();
    }
}