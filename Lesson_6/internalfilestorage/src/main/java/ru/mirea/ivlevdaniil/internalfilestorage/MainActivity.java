package ru.mirea.ivlevdaniil.internalfilestorage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "history_note.txt";
    private static final String LOG_TAG = "InternalStorage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editNote = findViewById(R.id.editNote);
        Button buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(v -> {
            String text = editNote.getText().toString();
            saveTextToFile(text);
        });
    }

    private void saveTextToFile(String text) {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(text.getBytes());
            Toast.makeText(this, "Сохранено в файл: " + FILE_NAME, Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Сохранено во внутреннее хранилище");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Ошибка записи файла", e);
            Toast.makeText(this, "Ошибка записи файла: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
