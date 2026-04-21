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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.neurolearn.R;
import com.example.neurolearn.utils.GroqHelper;

import java.io.InputStream;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

public class SummarizerFragment extends Fragment {

    private EditText inputText;
    private Button btnSummarize, btnUploadPDF;
    private android.widget.ImageButton btnBack;
    private TextView outputText;

    private static final int PICK_PDF = 1;

    public SummarizerFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_summarizer, container, false);

        inputText = view.findViewById(R.id.inputText);
        btnSummarize = view.findViewById(R.id.btnSummarize);
        btnUploadPDF = view.findViewById(R.id.btnUploadPDF);
        outputText = view.findViewById(R.id.outputText);
        btnBack = view.findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        btnSummarize.setOnClickListener(v -> summarizeText());
        btnUploadPDF.setOnClickListener(v -> pickPDF());

        return view;
    }

    // 🧠 SUMMARIZE
    private void summarizeText() {

        String notes = inputText.getText().toString().trim();

        if (TextUtils.isEmpty(notes)) {
            outputText.setText("⚠️ Please enter notes");
            return;
        }

        outputText.setText("⏳ Summarizing...");

        if (notes.length() > 20000) {
            notes = notes.substring(0, 20000);
        }

        String prompt = "Summarize the following content into simple bullet points:\n" + notes;

        GroqHelper.askQuestion(prompt, new GroqHelper.Callback() {
            @Override
            public void onResponse(String response) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> outputText.setText(response));
            }

            @Override
            public void onError(String error) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> outputText.setText("Error: " + error));
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
                extractTextFromPDF(pdfUri);
            }
        }
    }

    // 📖 EXTRACT TEXT (BACKGROUND THREAD)
    private void extractTextFromPDF(Uri uri) {

        outputText.setText("⏳ Reading PDF...");

        new Thread(() -> {
            try {
                InputStream inputStream = requireContext()
                        .getContentResolver()
                        .openInputStream(uri);

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
                    outputText.setText("✅ PDF loaded. You can now summarize or generate quiz.");
                    Toast.makeText(getContext(), "PDF Loaded!", Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                e.printStackTrace();

                if (getActivity() == null) return;

                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to read PDF", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

}