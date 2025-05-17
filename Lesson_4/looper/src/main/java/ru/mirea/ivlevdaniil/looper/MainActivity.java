package ru.mirea.ivlevdaniil.looper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import ru.mirea.ivlevdaniil.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Главный Handler
        Handler mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("result");
                Log.d("MainActivity", "Получено сообщение: " + result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        };

        // Создание и запуск фонового потока
        myLooper = new MyLooper(mainHandler);
        myLooper.start();

        // Обработка кнопки
        binding.buttonMirea.setOnClickListener(v -> {
            String ageStr = binding.editTextAge.getText().toString();
            String jobStr = binding.editTextJob.getText().toString();

            if (!ageStr.isEmpty() && !jobStr.isEmpty()) {
                int age = Integer.parseInt(ageStr);
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("AGE", age);
                bundle.putString("JOB", jobStr);
                msg.setData(bundle);

                if (myLooper.mHandler != null) {
                    myLooper.mHandler.sendMessage(msg);
                } else {
                    Toast.makeText(this, "Поток ещё не готов", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
