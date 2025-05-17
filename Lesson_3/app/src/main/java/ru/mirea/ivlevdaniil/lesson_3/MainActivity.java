package ru.mirea.ivlevdaniil.lesson_3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.mirea.ivlevdaniil.lesson_3.R;

public class MainActivity extends AppCompatActivity {

    Button button;
    int numberInGroup = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.buttonTime);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTimeToSecondActivity();
            }
        });
    }

    private void sendTimeToSecondActivity() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(new Date(currentTimeMillis));

        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("time", currentTime);
        intent.putExtra("number", numberInGroup);
        startActivity(intent);
    }
}
