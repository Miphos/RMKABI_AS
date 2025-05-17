package ru.mirea.ivlevdaniil.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private TextView textViewIp, textViewCity, textViewRegion, textViewCoords, textViewTemp, textViewWind;
    private Button button;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final String TAG = "HttpURLConnection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewIp = findViewById(R.id.textViewIp);
        textViewCity = findViewById(R.id.textViewCity);
        textViewRegion = findViewById(R.id.textViewRegion);
        textViewCoords = findViewById(R.id.textViewCoords);
        textViewTemp = findViewById(R.id.textViewTemp);
        textViewWind = findViewById(R.id.textViewWind);
        button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            if (isConnected()) {
                executor.execute(() -> {
                    try {
                        String ipInfo = downloadData("https://ipinfo.io/json");
                        JSONObject json = new JSONObject(ipInfo);

                        String ip = json.getString("ip");
                        String city = json.getString("city");
                        String region = json.getString("region");
                        String loc = json.getString("loc"); // "lat,lon"

                        String[] coords = loc.split(",");
                        String lat = coords[0];
                        String lon = coords[1];

                        String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat +
                                "&longitude=" + lon + "&current_weather=true";
                        String weatherInfo = downloadData(weatherUrl);

                        JSONObject weatherJson = new JSONObject(weatherInfo);
                        JSONObject current = weatherJson.getJSONObject("current_weather");

                        String temperature = current.getString("temperature");
                        String windspeed = current.getString("windspeed");

                        handler.post(() -> {
                            textViewIp.setText("IP: " + ip);
                            textViewCity.setText("Город: " + city);
                            textViewRegion.setText("Регион: " + region);
                            textViewCoords.setText("Координаты: " + lat + ", " + lon);
                            textViewTemp.setText("Температура: " + temperature + " °C");
                            textViewWind.setText("Скорость ветра: " + windspeed + " км/ч");
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Ошибка загрузки", e);
                        handler.post(() -> {
                            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                });
            } else {
                Toast.makeText(this, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    private String downloadData(String address) throws Exception {
        InputStream inputStream = null;
        String data;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Ошибка подключения: " + responseCode);
            }

            inputStream = connection.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read()) != -1) {
                bos.write(read);
            }
            data = bos.toString();
        } finally {
            if (inputStream != null) inputStream.close();
            if (connection != null) connection.disconnect();
        }
        return data;
    }
}
