package com.example.neurolearn.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.neurolearn.R;
import com.example.neurolearn.models.QuizQuestion;

import java.util.HashMap;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private List<QuizQuestion> questions;

    // Stores selected answers (position → selected option index)
    private HashMap<Integer, Integer> selectedAnswers = new HashMap<>();

    public QuizAdapter(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    public HashMap<Integer, Integer> getSelectedAnswers() {
        return selectedAnswers;
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

        holder.tvQuestion.setText(q.question);

        // Set options
        holder.option1.setText(q.options.get(0));
        holder.option2.setText(q.options.get(1));
        holder.option3.setText(q.options.get(2));
        holder.option4.setText(q.options.get(3));

        // Restore previous selection
        if (selectedAnswers.containsKey(position)) {
            int selected = selectedAnswers.get(position);
            ((RadioButton) holder.radioGroup.getChildAt(selected)).setChecked(true);
        } else {
            holder.radioGroup.clearCheck();
        }

        // Handle selection
        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            int selectedIndex = -1;

            if (checkedId == R.id.option1) selectedIndex = 0;
            else if (checkedId == R.id.option2) selectedIndex = 1;
            else if (checkedId == R.id.option3) selectedIndex = 2;
            else if (checkedId == R.id.option4) selectedIndex = 3;

            selectedAnswers.put(position, selectedIndex);
        });
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