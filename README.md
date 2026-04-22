# 🧠 NeuroLearn – AI-Powered Study Assistant

NeuroLearn is an intelligent Android application designed to assist students with learning using **AI-powered tools**. It integrates chatbot interaction, text summarization, PDF processing, and quiz generation into a single seamless experience.

---

## 🚀 Features

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

### 👤 Profile (Upcoming)

* User dashboard
* Progress tracking (planned)

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
| AI Integration | Groq API                      |
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

## 🧠 AI Integration

The app uses **Groq API** for:

* Chat responses
* Summarization
* Quiz generation

Example Prompt:

```
Generate 5 MCQ questions from the following content:
```

---

## 📂 PDF Processing

* Users can upload PDF files
* Text is extracted using **iText PDF library**
* Content is processed and sent to AI

---

## 🎤 Voice Input

* Uses Android `RecognizerIntent`
* Converts speech → text
* Autofills input field for queries

---

## 🎨 UI Highlights

* Card-based dashboard
* ChatGPT-style messaging UI
* RecyclerView-based dynamic lists
* Material Design components

---

## 📊 Current Capabilities

* ✅ AI Chat System
* ✅ AI Summarizer (Text + PDF)
* ✅ AI Quiz Generator
* ✅ Score Evaluation
* ✅ Voice Input

---

## 🛠️ Setup Instructions

1. Clone the repository:

```
git clone https://github.com/your-username/NeuroLearn.git
```

2. Open in Android Studio

3. Add your Groq API key in:

```
GroqHelper.java
```

4. Run the project on emulator/device

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
