package com.mirea.ivlevdaniil.who_am_i;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private TextView textViewStudent;
    private Button btnWhoAmI;
    private Button btnItIsNotMe;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация объектов
        textViewStudent = findViewById(R.id.tvOut);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe);
        checkBox = findViewById(R.id.checkBox);

        // Создание и установка обработчика для кнопки "btnWhoAmI"
        View.OnClickListener oclBtnWhoAmI = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewStudent.setText("Мой номер по списку № или 12, или 11, а может быть и 10");
                // Изменяем состояние CheckBox при нажатии на кнопку "btnWhoAmI"
                checkBox.setChecked(true);
            }
        };
        btnWhoAmI.setOnClickListener(oclBtnWhoAmI);

        // Создание и установка обработчика для кнопки "btnItIsNotMe" вторым способом
        btnItIsNotMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewStudent.setText("Это не я сделал");
                // Изменяем состояние CheckBox при нажатии на кнопку "btnItIsNotMe"
                checkBox.setChecked(false);
            }
        });
    }
}