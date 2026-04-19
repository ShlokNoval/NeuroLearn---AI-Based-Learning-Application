package com.example.neurolearn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neurolearn.MainActivity;
import com.example.neurolearn.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    Button btnSignup;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Enter name");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter valid email");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Minimum 6 characters");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    String userId = auth.getCurrentUser().getUid();

                    HashMap<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);

                    db.collection("users").document(userId).set(user);

                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Signup Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}