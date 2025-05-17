package ru.mirea.ivlevdaniil.mirea_project.ui.background;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {

    public static final String TAG = "MyWorker";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: START");

        try {
            TimeUnit.SECONDS.sleep(5);  // Имитация долгой работы
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted", e);
            return Result.failure();
        }

        Log.d(TAG, "doWork: END");
        return Result.success();
    }
}
