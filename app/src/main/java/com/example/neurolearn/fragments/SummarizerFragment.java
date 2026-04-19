package com.example.neurolearn.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.example.neurolearn.R;
import com.example.neurolearn.utils.GroqHelper;

public class SummarizerFragment extends Fragment {

    private EditText inputText;
    private Button btnSummarize, btnUploadPDF;
    private TextView outputText;

    private static final int PICK_PDF = 1;

    public SummarizerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_summarizer, container, false);

        inputText = view.findViewById(R.id.inputText);
        btnSummarize = view.findViewById(R.id.btnSummarize);
        btnUploadPDF = view.findViewById(R.id.btnUploadPDF);
        outputText = view.findViewById(R.id.outputText);

        btnSummarize.setOnClickListener(v -> summarizeText());
        btnUploadPDF.setOnClickListener(v -> pickPDF());

        return view;
    }

    private void summarizeText() {

        String notes = inputText.getText().toString().trim();

        if (TextUtils.isEmpty(notes)) {
            outputText.setText("⚠️ Please enter notes");
            return;
        }

        outputText.setText("⏳ Summarizing...");

        String prompt = "Summarize the following content into simple bullet points:\n" + notes;

        GroqHelper.askQuestion(prompt, new GroqHelper.Callback() {
            @Override
            public void onResponse(String response) {
                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> {
                    outputText.setText(response);
                });
            }

            @Override
            public void onError(String error) {
                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> {
                    outputText.setText("Error: " + error);
                });
            }
        });
    }

    // 📂 PICK PDF
    private void pickPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF);
    }

    // 📥 HANDLE RESULT
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF && resultCode == Activity.RESULT_OK && data != null) {

            Uri pdfUri = data.getData();

            if (pdfUri != null) {
                inputText.setText("PDF Selected:\n" + pdfUri.getLastPathSegment());
                Toast.makeText(getContext(), "PDF Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}