package ru.mirea.ivlevdaniil.serviceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.*;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1001;

    private ProgressBar progressBar;
    private Handler progressHandler;
    private Runnable updateProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();

        Button buttonPlay = findViewById(R.id.buttonPlay);
        Button buttonStop = findViewById(R.id.buttonStop);
        Button buttonLike = findViewById(R.id.buttonLike);
        progressBar = findViewById(R.id.progressBar);

        buttonPlay.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(this, PlayerService.class);
            ContextCompat.startForegroundService(this, serviceIntent);

            // Запуск отслеживания прогресса через полсекунды (ожидание инициализации MediaPlayer)
            new Handler(Looper.getMainLooper()).postDelayed(this::startProgressTracking, 500);
        });

        buttonStop.setOnClickListener(v -> {
            stopService(new Intent(this, PlayerService.class));
            stopProgressTracking();
        });

        buttonLike.setOnClickListener(v ->
                Toast.makeText(this, "😊 Спасибо за лайк!", Toast.LENGTH_SHORT).show());
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_CODE);
            }
        }
    }

    private void startProgressTracking() {
        MediaPlayer mp = PlayerService.mediaPlayerInstance;
        if (mp == null) return;

        int duration = mp.getDuration();
        progressBar.setMax(duration);

        progressHandler = new Handler(Looper.getMainLooper());
        updateProgress = new Runnable() {
            @Override
            public void run() {
                if (mp != null && mp.isPlaying()) {
                    progressBar.setProgress(mp.getCurrentPosition());
                    progressHandler.postDelayed(this, 500);
                } else {
                    progressBar.setProgress(0);
                }
            }
        };
        progressHandler.post(updateProgress);
    }

    private void stopProgressTracking() {
        if (progressHandler != null && updateProgress != null) {
            progressHandler.removeCallbacks(updateProgress);
            progressBar.setProgress(0);
        }
    }
}
