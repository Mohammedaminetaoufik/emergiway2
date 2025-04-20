package com.example.emergiway;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class ChatbotActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        // Placeholder: Add chatbot logic here
        TextView textView = findViewById(R.id.chatbotTextView);
        textView.setText("Welcome to the Chatbot page! (To be implemented)");
    }
}
