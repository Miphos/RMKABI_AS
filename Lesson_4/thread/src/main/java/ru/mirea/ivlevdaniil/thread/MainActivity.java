package ru.mirea.ivlevdaniil.thread;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import ru.mirea.ivlevdaniil.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Вывод информации о главном потоке
        Thread mainThread = Thread.currentThread();
        binding.textViewResult.setText("Имя потока: " + mainThread.getName());

        mainThread.setName("БИСО-02-20, № по списку: 11, Любимый фильм: Гостья из будущего");
        binding.textViewResult.append("\nНовое имя потока: " + mainThread.getName());
        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));

        // Обработка нажатия кнопки
        binding.buttonMirea.setOnClickListener(v -> {
            String totalStr = binding.editTextTotalPairs.getText().toString();
            String daysStr = binding.editTextStudyDays.getText().toString();

            if (totalStr.isEmpty() || daysStr.isEmpty()) {
                binding.textViewResult.setText("Пожалуйста, заполните оба поля.");
                return;
            }

            int total = Integer.parseInt(totalStr);
            int days = Integer.parseInt(daysStr);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int numberThread = counter++;

                    Log.d("ThreadProject", String.format("Запущен поток №%d студентом группы БИСО-02-20, номер по списку 11", numberThread));

                    try {
                        Thread.sleep(3000); // Симуляция долгой работы
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    double average = (double) total / days;
                    Log.d("ThreadProject", "Выполнен поток № " + numberThread);

                    runOnUiThread(() -> {
                        binding.textViewResult.setText("Среднее пар в день: " + String.format("%.2f", average));
                    });
                }
            }).start();
        });
    }
}
