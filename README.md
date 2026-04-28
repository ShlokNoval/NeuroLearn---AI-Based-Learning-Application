# 🧠 NeuroLearn – AI-Powered Study Assistant

NeuroLearn is an intelligent Android application designed to assist students with learning using **AI-powered tools**. It integrates chatbot interaction, text summarization, PDF processing, and quiz generation into a single seamless experience.

---

## 🚀 Introduction
NeuroLearn is an advanced, AI-powered study companion designed to modernize and simplify the learning process for students and professionals. Built with a focus on efficiency and engagement, the application leverages state-of-the-art Large Language Models (LLMs) like **Gemini 2.5 Flash** and **Groq** to transform static study materials into interactive learning tools.

The application offers a suite of intelligent features, including an **AI Summarizer** that condenses lengthy notes or PDF documents into digestible bullet points, and an **AI Quiz Generator** that instantly creates customized multiple-choice assessments from user-provided content. With integrated PDF support, secure authentication, and a real-time AI tutor (Chatbot), NeuroLearn acts as a personalized educational assistant that bridges the gap between passive reading and active knowledge retention.

---

## ❓ Problem Statement
In the modern academic landscape, students are frequently overwhelmed by the massive volume of information they must process daily. Traditional study methods—such as manual summarization, repetitive reading, and the creation of self-test materials—are often:

*   **Time-Consuming:** Spending hours extracting key points from dense textbooks or research papers reduces the time available for actual comprehension.
*   **Inefficient:** Passive reading often leads to poor long-term retention compared to active testing.
*   **Mentally Taxing:** Manually creating high-quality practice questions is difficult and requires significant cognitive effort before the studying even begins.

There is a critical need for an automated solution that can "comprehend" complex text, summarize key concepts, and provide immediate interactive feedback. **NeuroLearn** addresses these challenges by automating the heavy lifting of content processing, allowing learners to focus entirely on mastering the subject matter.

---

## ✨ Key Features

### 💬 AI Chat Assistant
* Ask questions and get instant answers
* ChatGPT-style interface
* Voice input support (Speech-to-Text)

### 📝 Smart Summarizer
* Summarize long notes into concise bullet points
* Upload PDFs and extract content automatically
* Generate summaries using AI

### 🧠 AI Quiz Generator
* Generate MCQs from text or PDF
* Interactive quiz interface
* Score calculation system
* Highlight correct & incorrect answers
* Animated score dialogs for feedback

### 👤 User Authentication
* Secure login and registration powered by **Firebase Auth**
* Profile management and persistent user sessions

---

## 🏗️ System Architecture

```
User Interface (XML)
        ↓
Fragments (Screens)
        ↓
Adapters (UI Logic)
        ↓
Models (Data Structure)
        ↓
Groq API (AI Engine)
```

---

## 🧩 Tech Stack

| Layer          | Technology                    |
| -------------- | ----------------------------- |
| Language       | Java                          |
| UI             | XML (Android Layouts)         |
| Architecture   | Fragment-based                |
| AI Integration | Groq API / Gemini Flash       |
| Backend        | Firebase (Auth, Firestore)      |
| PDF Processing | iText Library                 |
| Voice Input    | Android Speech API            |
| UI Components  | RecyclerView, Material Design |

---

## 📁 Project Structure

```
NeuroLearn/
│
├── fragments/
│   ├── HomeFragment.java
│   ├── ChatFragment.java
│   ├── SummarizerFragment.java
│   ├── QuizFragment.java
│   └── ProfileFragment.java
│
├── adapters/
│   ├── ChatAdapter.java
│   └── QuizAdapter.java
│
├── models/
│   ├── ChatMessage.java
│   └── QuizQuestion.java
│
├── utils/
│   └── GroqHelper.java
│   └── FirebaseHelper.java
│
├── res/layout/
│   ├── fragment_home.xml
│   ├── fragment_chat.xml
│   ├── fragment_summarizer.xml
│   ├── fragment_quiz.xml
│   └── item_quiz_question.xml
│
└── MainActivity.java
```

---

## ⚙️ How It Works

### 🔹 Chat System
* User enters query
* Request sent to Groq API
* AI response displayed in chat UI

### 🔹 Summarizer
* Input text or PDF uploaded
* Text extracted using iText
* AI generates summary

### 🔹 Quiz Generator
* AI generates MCQs in structured format
* App parses response into questions
* User answers → score calculated

---

## 🏆 Project Highlights
* Combines **AI + Mobile Development**
* Real-world problem solving for students
* Clean modular architecture
* Multiple AI use-cases in one app

---

## 👨‍💻 Author
**Shlok Noval**
B.Tech CSE (AIML)
Hackathon Enthusiast | AI Developer

---

## ⭐ If you like this project
Give it a ⭐ on GitHub and share your feedback!
