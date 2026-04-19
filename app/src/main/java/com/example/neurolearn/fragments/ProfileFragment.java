package com.example.neurolearn.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView tv = new TextView(getContext());
        tv.setText("👤 Profile Screen\n\nUser features coming soon");
        tv.setTextSize(18);
        tv.setPadding(40, 40, 40, 40);

        return tv;
    }
}