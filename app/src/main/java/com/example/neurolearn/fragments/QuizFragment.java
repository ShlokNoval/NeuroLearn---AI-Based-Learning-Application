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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

public class QuizFragment extends Fragment {

    private static final int PICK_PDF = 1;
    private EditText inputText;
    private Button btnUploadPDF, btnGenerate, btnSubmit;
    private android.widget.ImageButton btnBack;
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
        btnUploadPDF = view.findViewById(R.id.btnUploadPDF);
        btnGenerate = view.findViewById(R.id.btnGenerate);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        recyclerQuiz = view.findViewById(R.id.recyclerQuiz);
        btnBack = view.findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        if (btnUploadPDF != null) {
            btnUploadPDF.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, PICK_PDF);
            });
        }

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

        // 🔥 SHOW COLORS NOW
        adapter.showResults();

        showAnimatedScoreDialog(score, questionList.size());
    }

    private void showAnimatedScoreDialog(int score, int total) {
        if (getActivity() == null) return;
        
        android.app.Dialog dialog = new android.app.Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_score);
        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        
        android.widget.TextView tvScore = dialog.findViewById(R.id.tvScore);
        android.widget.TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        Button btnClose = dialog.findViewById(R.id.btnCloseDialog);
        
        tvScore.setText(score + " / " + total);
        
        if (score == total) {
            tvMessage.setText("Perfect Score! 🌟");
        } else if (score >= total / 2.0) {
            tvMessage.setText("Great Job! 👍");
        } else {
            tvMessage.setText("Keep Practicing! 📚");
        }
        
        btnClose.setOnClickListener(v -> dialog.dismiss());
        
        // Cool scale animation when dialog shows up
        dialog.setOnShowListener(d -> {
            android.view.View contentView = dialog.findViewById(android.R.id.content);
            if (contentView != null) {
                contentView.setScaleX(0.5f);
                contentView.setScaleY(0.5f);
                contentView.setAlpha(0f);
                contentView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(400)
                        .setInterpolator(new android.view.animation.OvershootInterpolator())
                        .start();
            }
        });
        
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF && resultCode == Activity.RESULT_OK && data != null) {
            Uri pdfUri = data.getData();
            if (pdfUri != null) {
                extractTextFromPDF(pdfUri);
            }
        }
    }

    private void extractTextFromPDF(Uri uri) {
        Toast.makeText(getContext(), "Reading PDF...", Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                PdfReader reader = new PdfReader(inputStream);
                PdfDocument pdfDoc = new PdfDocument(reader);
                StringBuilder text = new StringBuilder();

                for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                    text.append(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i)));
                }

                pdfDoc.close();
                inputStream.close();

                String extracted = text.toString();
                if (extracted.length() > 20000) {
                    extracted = extracted.substring(0, 20000);
                }

                if (getActivity() == null) return;
                String finalText = extracted;

                getActivity().runOnUiThread(() -> {
                    inputText.setText(finalText);
                    Toast.makeText(getContext(), "PDF Loaded! You can now generate the quiz.", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed to read PDF", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        }).start();
    }
}