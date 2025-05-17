package ru.mirea.ivlevdaniil.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        this.mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        Log.d("MyLooper", "Поток запущен");
        Looper.prepare();

        mHandler = new Handler(msg -> {
            Bundle b = msg.getData();
            int age = b.getInt("AGE");
            String job = b.getString("JOB");

            try {
                Thread.sleep(age * 1000L); // Задержка равна возрасту
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Возврат результата в основной поток
            Message message = Message.obtain();
            Bundle result = new Bundle();
            result.putString("result", "Возраст: " + age + ", Профессия: " + job);
            message.setData(result);
            mainHandler.sendMessage(message);

            return true;
        });

        Looper.loop();
    }
}
