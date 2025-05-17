package ru.mirea.ivlevdaniil.simplefragmentapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    Fragment firstFragment = new FirstFragment();
    Fragment secondFragment = new SecondFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Проверяем ориентацию экрана
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            // Загружаем первый фрагмент по умолчанию
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, firstFragment)
                    .commit();

            // Настраиваем кнопки
            Button btnFirst = findViewById(R.id.btnFirstFragment);
            Button btnSecond = findViewById(R.id.btnSecondFragment);

            btnFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, firstFragment)
                            .commit();
                }
            });

            btnSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, secondFragment)
                            .commit();
                }
            });
        }

    }
}
