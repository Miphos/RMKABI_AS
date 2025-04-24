package com.mirea.ivlevdaniil.multiactivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Получаем Intent, который запустил эту Activity
        Intent intent = getIntent();

        // Извлекаем данные по ключу "key"
        String receivedText = intent.getStringExtra("key");

        // Находим TextView и устанавливаем текст
        TextView textView = findViewById(R.id.textViewReceivedData);
        textView.setText(receivedText);
    }



}