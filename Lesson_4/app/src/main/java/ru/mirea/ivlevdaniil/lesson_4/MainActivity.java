package ru.mirea.ivlevdaniil.lesson_4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.ivlevdaniil.lesson_4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Устанавливаем текст в EditText
        binding.editTextMirea.setText("Мой номер по списку №___");

        // Обработка нажатия кнопки
        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(MainActivity.class.getSimpleName(), "onClickListener сработал");

                // Считываем текст из EditText и устанавливаем в TextView
                String inputText = binding.editTextMirea.getText().toString();
                binding.textViewMirea.setText("Вы ввели: " + inputText);
            }
        });
    }
}
