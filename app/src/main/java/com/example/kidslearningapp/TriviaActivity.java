package com.example.kidslearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TriviaActivity extends AppCompatActivity {

    private TextView tvQuestion, tvScore, tvSummary;
    private Button btnAnswer1, btnAnswer2, btnBack, btnNextSet;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<String> results;
    private int questionSetIndex = 0;
    private static final int QUESTIONS_PER_SET = 5;
    private String username;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        dbHelper = new DatabaseHelper(this);
        username = getIntent().getStringExtra("username"); // Passed from login

        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        tvSummary = findViewById(R.id.tvSummary);
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnBack = findViewById(R.id.btnBack);
        btnNextSet = findViewById(R.id.btnNextSet);

        // Hide unnecessary elements initially
        tvScore.setVisibility(View.GONE);
        tvSummary.setVisibility(View.GONE);
        btnBack.setVisibility(View.GONE);
        btnNextSet.setVisibility(View.GONE);

        questions = new ArrayList<>();
        results = new ArrayList<>();
        loadQuestions();

        Collections.shuffle(questions);
        showNextQuestion();

        btnAnswer1.setOnClickListener(v -> checkAnswer(btnAnswer1.getText().toString()));
        btnAnswer2.setOnClickListener(v -> checkAnswer(btnAnswer2.getText().toString()));
        btnNextSet.setOnClickListener(v -> startNextSet());
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(TriviaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadQuestions() {
        questions.add(new Question("What is the color of the sky?", "Blue", "Green"));
        questions.add(new Question("How many legs does a spider have?", "8", "6"));
        questions.add(new Question("What do bees make?", "Honey", "Milk"));
        questions.add(new Question("What is 2 + 2?", "4", "5"));
        questions.add(new Question("What animal says 'Meow'?", "Cat", "Dog"));
        questions.add(new Question("Which fruit is red and round?", "Apple", "Banana"));
        questions.add(new Question("What is the opposite of hot?", "Cold", "Warm"));
        questions.add(new Question("What is the capital of France?", "Paris", "London"));
        questions.add(new Question("Which animal has a long trunk?", "Elephant", "Giraffe"));
        questions.add(new Question("What do we drink that comes from cows?", "Milk", "Water"));
        questions.add(new Question("What is the shape of a wheel?", "Circle", "Square"));
        questions.add(new Question("How many fingers do we have on one hand?", "5", "4"));
        questions.add(new Question("What do we use to see?", "Eyes", "Ears"));
        questions.add(new Question("What do we use to hear?", "Ears", "Mouth"));
        questions.add(new Question("Which animal hops and has a pouch?", "Kangaroo", "Lion"));
        questions.add(new Question("What color are bananas?", "Yellow", "Purple"));
        questions.add(new Question("What is the name of our planet?", "Earth", "Mars"));
        questions.add(new Question("Which animal is known as the 'King of the Jungle'?", "Lion", "Tiger"));
        questions.add(new Question("What do plants need to grow?", "Water", "Sand"));
        questions.add(new Question("Which is bigger, an elephant or a mouse?", "Elephant", "Mouse"));
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < (questionSetIndex + 1) * QUESTIONS_PER_SET) {
            Question question = questions.get(currentQuestionIndex);
            tvQuestion.setText(question.getQuestion());
            btnAnswer1.setText(question.getCorrectAnswer());
            btnAnswer2.setText(question.getWrongAnswer());
        } else {
            showResults();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        Question question = questions.get(currentQuestionIndex);
        if (selectedAnswer.equals(question.getCorrectAnswer())) {
            score++;
            results.add("✔ " + question.getQuestion() + " - " + question.getCorrectAnswer());
        } else {
            results.add("✘ " + question.getQuestion() + " - Correct: " + question.getCorrectAnswer());
        }
        currentQuestionIndex++;
        btnAnswer1.postDelayed(this::showNextQuestion, 1000);
    }


    private void showResults() {
        tvQuestion.setText("Quiz Completed!");

        btnAnswer1.setVisibility(View.GONE);
        btnAnswer2.setVisibility(View.GONE);

        // Calculate the total score and reward points
        int totalQuestionsAnswered = (questionSetIndex + 1) * QUESTIONS_PER_SET;
        int rewardPoints = score * 10;

        // Update user's reward in the database
        dbHelper.updateUserScore(username, rewardPoints);

        // Display score and reward
        tvScore.setText("Your Score: " + score + "/" + totalQuestionsAnswered + "\n" +
                "Reward: " + rewardPoints + " points");
        tvScore.setVisibility(View.VISIBLE);

        // Display the earned points message
        TextView tvEarned = findViewById(R.id.tvEarned); // Make sure you have this TextView in XML
        tvEarned.setText("You just earned " + rewardPoints + " points by completing the quiz!");
        tvEarned.setVisibility(View.VISIBLE);  // Make sure this is set to VISIBLE

        // Build the result summary
        StringBuilder summaryText = new StringBuilder();
        for (String result : results) {
            summaryText.append(result).append("\n");
        }
        tvSummary.setText(summaryText.toString());
        tvSummary.setVisibility(View.VISIBLE);

        // Check if perfect score in current set
        int currentSetScore = score - (questionSetIndex * QUESTIONS_PER_SET);
        if (currentSetScore == 5 && questionSetIndex < 3) {
            btnNextSet.setVisibility(View.VISIBLE);
        } else {
            // Show the back button after displaying results
            btnBack.setVisibility(View.VISIBLE);
        }


    }


    private void startNextSet() {
        questionSetIndex++; // Move to the next question set
        currentQuestionIndex = questionSetIndex * QUESTIONS_PER_SET;
        results.clear(); // Clear previous round's results

        btnNextSet.setVisibility(View.GONE);
        tvScore.setVisibility(View.GONE);
        tvSummary.setVisibility(View.GONE);
        btnAnswer1.setVisibility(View.VISIBLE);
        btnAnswer2.setVisibility(View.VISIBLE);

        showNextQuestion();
    }

    private static class Question {
        private final String question;
        private final String correctAnswer;
        private final String wrongAnswer;

        public Question(String question, String correctAnswer, String wrongAnswer) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.wrongAnswer = wrongAnswer;
        }

        public String getQuestion() { return question; }
        public String getCorrectAnswer() { return correctAnswer; }
        public String getWrongAnswer() { return wrongAnswer; }
    }
}
