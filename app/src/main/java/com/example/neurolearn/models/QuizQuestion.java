package com.example.neurolearn.models;

import java.util.List;

public class QuizQuestion {

    public String question;
    public List<String> options;
    public String answer;

    public QuizQuestion(String question, List<String> options, String answer) {
        this.question = question;
        this.options = options;
        this.answer = answer;
    }
}