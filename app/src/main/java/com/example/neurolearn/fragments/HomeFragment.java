package com.example.neurolearn.fragments;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.neurolearn.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // CHAT (WORKING)
        view.findViewById(R.id.cardChat).setOnClickListener(v ->
                openFragment(new ChatFragment()));

        // SUMMARY (SAFE)
        view.findViewById(R.id.cardSummary).setOnClickListener(v -> {
            try {
                openFragment(new SummarizerFragment());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Summary coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        // QUIZ (SAFE)
        view.findViewById(R.id.cardQuiz).setOnClickListener(v -> {
            try {
                openFragment(new QuizFragment());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Quiz coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        // PROFILE (SAFE)
        view.findViewById(R.id.cardProfile).setOnClickListener(v -> {
            try {
                openFragment(new ProfileFragment());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Profile coming soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}