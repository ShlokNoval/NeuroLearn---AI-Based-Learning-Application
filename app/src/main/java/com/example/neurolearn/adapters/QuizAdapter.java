package com.example.neurolearn.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.neurolearn.R;
import com.example.neurolearn.models.QuizQuestion;

import java.util.HashMap;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private List<QuizQuestion> questions;
    private HashMap<Integer, Integer> selectedAnswers = new HashMap<>();

    // 🔥 NEW: flag to show results
    private boolean showResults = false;

    public QuizAdapter(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    public HashMap<Integer, Integer> getSelectedAnswers() {
        return selectedAnswers;
    }

    // 🔥 CALL THIS AFTER SUBMIT
    public void showResults() {
        showResults = true;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        QuizQuestion q = questions.get(position);

        holder.tvQuestion.setText("Q" + (position + 1) + ". " + q.question);

        holder.option1.setText(q.options.get(0));
        holder.option2.setText(q.options.get(1));
        holder.option3.setText(q.options.get(2));
        holder.option4.setText(q.options.get(3));

        // Reset colors
        resetColors(holder);

        holder.radioGroup.setOnCheckedChangeListener(null);

        // Restore selection
        if (selectedAnswers.containsKey(position)) {
            int selected = selectedAnswers.get(position);
            if (selected >= 0 && selected < holder.radioGroup.getChildCount()) {
                ((RadioButton) holder.radioGroup.getChildAt(selected)).setChecked(true);
            }
        } else {
            holder.radioGroup.clearCheck();
        }

        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedIndex = -1;

            if (checkedId == R.id.option1) selectedIndex = 0;
            else if (checkedId == R.id.option2) selectedIndex = 1;
            else if (checkedId == R.id.option3) selectedIndex = 2;
            else if (checkedId == R.id.option4) selectedIndex = 3;

            selectedAnswers.put(position, selectedIndex);
        });

        // 🔥 SHOW RESULTS LOGIC
        if (showResults) {

            // Disable further changes
            for (int i = 0; i < holder.radioGroup.getChildCount(); i++) {
                holder.radioGroup.getChildAt(i).setEnabled(false);
            }

            int correctIndex = getCorrectIndex(q.answer);

            // Highlight correct answer
            RadioButton correctBtn =
                    (RadioButton) holder.radioGroup.getChildAt(correctIndex);
            correctBtn.setBackgroundColor(Color.parseColor("#A5D6A7")); // green

            // If user selected wrong → mark red
            if (selectedAnswers.containsKey(position)) {
                int selected = selectedAnswers.get(position);

                if (selected != correctIndex) {
                    RadioButton selectedBtn =
                            (RadioButton) holder.radioGroup.getChildAt(selected);
                    selectedBtn.setBackgroundColor(Color.parseColor("#EF9A9A")); // red
                }
            }
        }
    }

    private int getCorrectIndex(String answer) {
        if (answer.equalsIgnoreCase("A")) return 0;
        if (answer.equalsIgnoreCase("B")) return 1;
        if (answer.equalsIgnoreCase("C")) return 2;
        if (answer.equalsIgnoreCase("D")) return 3;
        return -1;
    }

    private void resetColors(ViewHolder holder) {
        for (int i = 0; i < holder.radioGroup.getChildCount(); i++) {
            holder.radioGroup.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        RadioGroup radioGroup;
        RadioButton option1, option2, option3, option4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            radioGroup = itemView.findViewById(R.id.radioGroup);

            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
            option3 = itemView.findViewById(R.id.option3);
            option4 = itemView.findViewById(R.id.option4);
        }
    }
}