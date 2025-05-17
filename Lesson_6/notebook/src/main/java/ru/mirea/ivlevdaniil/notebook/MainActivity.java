package ru.mirea.ivlevdaniil.notebook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.*;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "NotebookApp";

    private EditText editFileName, editQuote;
    private Button buttonSave, buttonLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editFileName = findViewById(R.id.editFileName);
        editQuote = findViewById(R.id.editQuote);
        buttonSave = findViewById(R.id.buttonSave);
        buttonLoad = findViewById(R.id.buttonLoad);

        // Запрос разрешений
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        buttonSave.setOnClickListener(v -> writeToFile());
        buttonLoad.setOnClickListener(v -> readFromFile());
    }

    private void writeToFile() {
        String fileName = editFileName.getText().toString();
        String quote = editQuote.getText().toString();

        if (fileName.isEmpty() || quote.isEmpty()) {
            Toast.makeText(this, "Введите название файла и цитату", Toast.LENGTH_SHORT).show();
            return;
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName + ".txt");

        try {
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(quote);
            writer.close();
            Toast.makeText(this, "Файл сохранён: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.w(LOG_TAG, "Ошибка при записи файла", e);
            Toast.makeText(this, "Ошибка записи: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void readFromFile() {
        String fileName = editFileName.getText().toString();

        if (fileName.isEmpty()) {
            Toast.makeText(this, "Введите название файла", Toast.LENGTH_SHORT).show();
            return;
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName + ".txt");

        try {
            FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            editQuote.setText(builder.toString().trim());
            Toast.makeText(this, "Файл загружен", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.w(LOG_TAG, "Ошибка при чтении файла", e);
            Toast.makeText(this, "Ошибка чтения: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
