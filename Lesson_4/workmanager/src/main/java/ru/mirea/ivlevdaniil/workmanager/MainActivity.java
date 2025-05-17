package ru.mirea.ivlevdaniil.workmanager;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class MainActivity extends AppCompatActivity {

    Button btnStartWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartWork = findViewById(R.id.btnStartWork);

        btnStartWork.setOnClickListener(v -> {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED) // Wi-Fi
                    .build();

            WorkRequest uploadRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(this).enqueue(uploadRequest);
        });
    }
}
