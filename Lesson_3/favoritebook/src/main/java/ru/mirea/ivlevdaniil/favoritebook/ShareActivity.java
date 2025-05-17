package ru.mirea.ivlevdaniil.favoritebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {

    private EditText editBook, editQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        editBook = findViewById(R.id.editBook);
        editQuote = findViewById(R.id.editQuote);
        Button buttonSend = findViewById(R.id.buttonSend);

        // Получаем данные разработчика (необязательно)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String book = extras.getString(MainActivity.BOOK_NAME_KEY);
            String quote = extras.getString(MainActivity.QUOTES_KEY);

            TextView devBook = findViewById(R.id.editBook);
            TextView devQuote = findViewById(R.id.editQuote);
            devBook.setHint("Пример: " + book);
            devQuote.setHint("Пример: " + quote);
        }

        buttonSend.setOnClickListener(v -> {
            String book = editBook.getText().toString().trim();
            String quote = editQuote.getText().toString().trim();

            String result = "Название Вашей любимой книги: " + book + ". Цитата: " + quote;

            Intent data = new Intent();
            data.putExtra(MainActivity.USER_MESSAGE, result);
            setResult(Activity.RESULT_OK, data);
            finish();
        });
    }
}