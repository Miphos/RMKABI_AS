package ru.mirea.ivlevdaniil.lesson_7;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TimeService";
    private final String host = "time.nist.gov";
    private final int port = 13;

    private TextView textViewResult;
    private Button button;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.textViewResult);
        button = findViewById(R.id.button);

        button.setOnClickListener(v -> executorService.execute(() -> {
            String timeLine = "";
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine(); // игнорируем первую строку
                timeLine = reader.readLine(); // получаем строку с датой и временем
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                timeLine = "Ошибка: " + e.getMessage();
            }

            String finalTimeLine = timeLine;
            mainThreadHandler.post(() -> {
                Log.d(TAG, finalTimeLine);
                StringBuilder result = new StringBuilder();

                String[] parts = finalTimeLine.split(" ");
                if (parts.length >= 9) {
                    result.append("Полный ответ сервера: ").append(finalTimeLine).append("\n\n")
                            .append("Номер дня с 1900 года: ").append(parts[0]).append("\n")
                            .append("Дата: ").append(parts[1]).append("\n")
                            .append("Время: ").append(parts[2]).append("\n")
                            .append("Секундное смещение: ").append(parts[3]).append("\n")
                            .append("Состояние сервера: ").append(parts[4]).append("\n")
                            .append("Уровень доверия: ").append(parts[5]).append("\n")
                            .append("Отклонение (мс): ").append(parts[6]).append("\n")
                            .append("Источник времени: ").append(parts[7]).append("\n")
                            .append("Флаг точности: ").append(parts[8]).append("\n");
                } else {
                    result.append("Не удалось разобрать строку:\n").append(finalTimeLine);
                }

                textViewResult.setText(result.toString());
            });
        }));
    }
}
