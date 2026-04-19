package com.example.neurolearn.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.neurolearn.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize buttons
        Button btnChat = view.findViewById(R.id.btnChat);
        Button btnSummarize = view.findViewById(R.id.btnSummarize);
        Button btnQuiz = view.findViewById(R.id.btnQuiz);
        Button btnProfile = view.findViewById(R.id.btnProfile);

        // Navigation logic
        btnChat.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new ChatFragment())
                        .commit()
        );

        btnSummarize.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new SummarizerFragment())
                        .commit()
        );

        btnQuiz.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new QuizFragment())
                        .commit()
        );

        btnProfile.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new ProfileFragment())
                        .commit()
        );
    }
}