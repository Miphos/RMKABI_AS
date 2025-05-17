package ru.mirea.ivlevdaniil.data_thread;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.TimeUnit;
import ru.mirea.ivlevdaniil.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Создание Runnable задач
        final Runnable runn1 = () -> binding.tvInfo.setText("runn1 (runOnUiThread)");
        final Runnable runn2 = () -> binding.tvInfo.setText("runn2 (post)");
        final Runnable runn3 = () -> binding.tvInfo.setText("runn3 (postDelayed)");

        // Поток, который будет запускать эти задачи с задержками
        Thread t = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                runOnUiThread(runn1);  // безопасно обновляем UI

                TimeUnit.SECONDS.sleep(1);
                binding.tvInfo.post(runn2);  // безопасный способ через View

                binding.tvInfo.postDelayed(runn3, 2000);  // отложенное выполнение
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.start();
    }
}
