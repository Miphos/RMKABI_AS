package ru.mirea.ivlevdaniil.employeedb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editName, editPower;
    private Button buttonAdd, buttonLoad;
    private TextView textOutput;

    private EmployeeDao employeeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editPower = findViewById(R.id.editPower);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonLoad = findViewById(R.id.buttonLoad);
        textOutput = findViewById(R.id.textOutput);

        AppDatabase db = App.getInstance().getDatabase();
        employeeDao = db.employeeDao();

        buttonAdd.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String power = editPower.getText().toString().trim();

            if (name.isEmpty() || power.isEmpty()) {
                Toast.makeText(this, "Введите имя и суперсилу", Toast.LENGTH_SHORT).show();
                return;
            }

            Employee hero = new Employee();
            hero.name = name;
            hero.superpower = power;

            employeeDao.insert(hero);
            Toast.makeText(this, "Герой добавлен", Toast.LENGTH_SHORT).show();

            editName.setText("");
            editPower.setText("");
        });

        buttonLoad.setOnClickListener(v -> {
            List<Employee> heroes = employeeDao.getAll();
            if (heroes.isEmpty()) {
                textOutput.setText("Нет героев в базе");
            } else {
                StringBuilder builder = new StringBuilder();
                for (Employee e : heroes) {
                    builder.append("ID: ").append(e.id)
                            .append("\nИмя: ").append(e.name)
                            .append("\nСуперсила: ").append(e.superpower)
                            .append("\n\n");
                }
                textOutput.setText(builder.toString());
            }
        });
    }
}
