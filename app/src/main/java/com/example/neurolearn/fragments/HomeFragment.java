package com.example.neurolearn.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.neurolearn.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. TOP RIGHT PROFILE WIDGET (New)
        view.findViewById(R.id.ivProfileWidget).setOnClickListener(v -> navigateToProfile());

        // 2. CHAT CARD
        view.findViewById(R.id.cardChat).setOnClickListener(v ->
                openFragment(new ChatFragment()));

        // 3. SUMMARY CARD
        view.findViewById(R.id.cardSummary).setOnClickListener(v -> {
            try {
                openFragment(new SummarizerFragment());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Summary coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. QUIZ CARD
        view.findViewById(R.id.cardQuiz).setOnClickListener(v -> {
            try {
                openFragment(new QuizFragment());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Quiz coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        // 5. PROFILE CARD
        view.findViewById(R.id.cardProfile).setOnClickListener(v -> navigateToProfile());
    }

    /**
     * Helper method to handle Profile Navigation from multiple sources
     */
    private void navigateToProfile() {
        try {
            openFragment(new ProfileFragment());
        } catch (Exception e) {
            Toast.makeText(getContext(), "Profile coming soon", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Standard method to handle Fragment Transactions with animations
     */
    private void openFragment(Fragment fragment) {
        if (getActivity() != null) {
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
}