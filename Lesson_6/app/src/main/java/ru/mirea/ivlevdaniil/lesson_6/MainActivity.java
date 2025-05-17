package ru.mirea.ivlevdaniil.lesson_6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editGroup, editNumber, editMovie;
    private static final String PREF_NAME = "mirea_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editGroup = findViewById(R.id.editGroup);
        editNumber = findViewById(R.id.editNumber);
        editMovie = findViewById(R.id.editMovie);
        Button saveButton = findViewById(R.id.buttonSave);

        // Загрузка сохранённых данных при старте приложения
        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String group = sharedPref.getString("GROUP", "");
        int number = sharedPref.getInt("NUMBER", 0);
        String movie = sharedPref.getString("FAVORITE_MOVIE", "");

        editGroup.setText(group);
        editNumber.setText(String.valueOf(number));
        editMovie.setText(movie);

        // Сохранение данных по нажатию кнопки
        saveButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("GROUP", editGroup.getText().toString());
            editor.putInt("NUMBER", Integer.parseInt(editNumber.getText().toString()));
            editor.putString("FAVORITE_MOVIE", editMovie.getText().toString());
            editor.apply();
        });
    }
}
