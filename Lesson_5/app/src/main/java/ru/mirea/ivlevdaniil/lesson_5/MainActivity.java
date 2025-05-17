package ru.mirea.ivlevdaniil.lesson_5;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Используем ViewBinding
        setContentView(R.layout.activity_main);

        // Получение SensorManager
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // Находим ListView
        listSensor = findViewById(R.id.list_view);

        // создаем список для отображения в ListView найденных датчиков
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        for (Sensor sensor : sensors) {
            HashMap<String, Object> sensorTypeList = new HashMap<>();
            sensorTypeList.put("Name", sensor.getName());
            sensorTypeList.put("Value", sensor.getMaximumRange());
            arrayList.add(sensorTypeList);
        }

        // создаем адаптер и устанавливаем тип адаптера — отображение двух полей
        SimpleAdapter mHistory =
                new SimpleAdapter(this, arrayList,
                        android.R.layout.simple_list_item_2,
                        new String[]{"Name", "Value"},
                        new int[]{android.R.id.text1, android.R.id.text2});

        // Устанавливаем адаптер
        listSensor.setAdapter(mHistory);
    }
}
