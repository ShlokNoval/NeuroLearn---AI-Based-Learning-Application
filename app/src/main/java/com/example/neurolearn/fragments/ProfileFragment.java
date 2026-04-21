package com.example.neurolearn.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.neurolearn.R;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the back button by the ID defined in fragment_profile.xml
        ImageButton backBtn = view.findViewById(R.id.btnBackProfile);

        if (backBtn != null) {
            backBtn.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        android.widget.TextView tvUserName = view.findViewById(R.id.tvUserName);
        android.widget.TextView tvUserEmail = view.findViewById(R.id.tvUserEmail);
        android.widget.Button btnLogout = view.findViewById(R.id.btnLogout);

        com.google.firebase.auth.FirebaseAuth auth = com.google.firebase.auth.FirebaseAuth.getInstance();
        com.google.firebase.auth.FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            com.google.firebase.firestore.FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && getActivity() != null) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            tvUserName.setText(name != null ? name : "Student User");
                            tvUserEmail.setText(email != null ? email : "ID: NEURO-99210");
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (getActivity() != null) {
                            android.widget.Toast.makeText(getContext(), "Failed to load profile", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                if (getActivity() != null) {
                    auth.signOut();
                    android.content.Intent intent = new android.content.Intent(getActivity(), com.example.neurolearn.LoginActivity.class);
                    intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
    }
}