package ru.mirea.ivlevdaniil.favoritebook;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String USER_MESSAGE = "MESSAGE";
    public static final String BOOK_NAME_KEY = "book_name";
    public static final String QUOTES_KEY = "quotes_name";

    private TextView textViewUserBook;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewUserBook = findViewById(R.id.textViewBook);
        Button buttonOpen = findViewById(R.id.buttonOpen);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String message = data.getStringExtra(USER_MESSAGE);
                            textViewUserBook.setText(message);
                        }
                    }
                }
        );

        buttonOpen.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShareActivity.class);
            intent.putExtra(BOOK_NAME_KEY, "Преступление и наказание");
            intent.putExtra(QUOTES_KEY, "При неудаче все кажется глупо");
            activityResultLauncher.launch(intent);
        });
    }
}