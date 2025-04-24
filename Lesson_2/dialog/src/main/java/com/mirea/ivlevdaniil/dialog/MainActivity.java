package com.mirea.ivlevdaniil.dialog;

import android.app.*;
import android.content.DialogInterface;
import android.os.*;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AlertDialog
        findViewById(R.id.btnAlertDialog).setOnClickListener(v -> showAlertDialog());

        // DatePickerDialog
        findViewById(R.id.btnDatePicker).setOnClickListener(v -> showDatePickerDialog());

        // TimePickerDialog
        findViewById(R.id.btnTimePicker).setOnClickListener(v -> showTimePickerDialog());

        // ProgressDialog
        findViewById(R.id.btnProgressDialog).setOnClickListener(v -> showProgressDialog());

        // Snackbar
        findViewById(R.id.btnSnackbar).setOnClickListener(v -> showSnackbar());
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Диалоговое окно")
                .setMessage("Выберите действие")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("OK", (dialog, which) -> {
                    Toast.makeText(this, "Нажата кнопка OK", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                    Toast.makeText(this, "Нажата кнопка Отмена", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("Позже", (dialog, which) -> {
                    Toast.makeText(this, "Нажата кнопка Позже", Toast.LENGTH_SHORT).show();
                })
                .setCancelable(false)
                .show();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "." + (month + 1) + "." + year;
                    Toast.makeText(this, "Выбрана дата: " + selectedDate, Toast.LENGTH_SHORT).show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String selectedTime = hourOfDay + ":" + minute;
                    Toast.makeText(this, "Выбрано время: " + selectedTime, Toast.LENGTH_SHORT).show();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void showProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Загрузка");
        progressDialog.setMessage("Пожалуйста, подождите...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.show();

        // Имитация загрузки
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(50);
                    progressDialog.setProgress(i);
                }
                progressDialog.dismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showSnackbar() {
        Snackbar.make(findViewById(android.R.id.content),
                        "Это Snackbar уведомление",
                        Snackbar.LENGTH_LONG)
                .setAction("Действие", v -> {
                    Toast.makeText(this, "Действие выполнено", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}