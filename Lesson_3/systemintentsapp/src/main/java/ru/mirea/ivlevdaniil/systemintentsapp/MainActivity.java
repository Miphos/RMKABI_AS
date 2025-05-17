package ru.mirea.ivlevdaniil.systemintentsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCall = findViewById(R.id.buttonCall);
        Button buttonBrowser = findViewById(R.id.buttonBrowser);
        Button buttonMap = findViewById(R.id.buttonMap);

        // Вызов номера
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:89811112233"));
                startActivity(callIntent);
            }
        });

        // Открытие браузера
        buttonBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://developer.android.com"));
                startActivity(browserIntent);
            }
        });

        // Открытие карты
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setData(Uri.parse("geo:55.749479,37.613944"));
                startActivity(mapIntent);
            }
        });
    }
}
