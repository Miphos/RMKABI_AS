package ru.mirea.ivlevdaniil.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.ivlevdaniil.securesharedpreferences.R;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "secure_shared_prefs";

    private EditText editPoet;
    private Button saveButton;
    private ImageView imagePoet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPoet = findViewById(R.id.editPoet);
        saveButton = findViewById(R.id.buttonSavePoet);
        imagePoet = findViewById(R.id.imagePoet);

        try {
            // Получение мастер-ключа
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // Создание защищённых SharedPreferences
            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    FILE_NAME,
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // Загрузка ранее сохранённого значения
            String poet = secureSharedPreferences.getString("secure_poet", "");
            editPoet.setText(poet);

            // Установка изображения поэта
            imagePoet.setImageResource(R.drawable.pushkin); // замените на ваше изображение в drawable

            // Обработчик кнопки "Сохранить"
            saveButton.setOnClickListener(v -> {
                SharedPreferences.Editor editor = secureSharedPreferences.edit();
                editor.putString("secure_poet", editPoet.getText().toString());
                editor.apply();
            });

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
