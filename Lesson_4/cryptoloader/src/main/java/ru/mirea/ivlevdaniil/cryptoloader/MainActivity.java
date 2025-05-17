package ru.mirea.ivlevdaniil.cryptoloader;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.mirea.ivlevdaniil.cryptoloader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private ActivityMainBinding binding;
    private final int LoaderID = 123;
    private byte[] encryptedData;
    private byte[] secretKeyBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonEncrypt.setOnClickListener(v -> {
            String phrase = binding.editTextPhrase.getText().toString();

            if (phrase.isEmpty()) {
                Toast.makeText(this, "Введите фразу", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Генерация ключа
            SecretKey key = generateKey();
            secretKeyBytes = key.getEncoded();

            // 2. Шифрование
            encryptedData = encryptText(phrase, key);

            // 3. Передача в Bundle и запуск Loader
            Bundle args = new Bundle();
            args.putByteArray(MyLoader.ARG_ENCRYPTED, encryptedData);
            args.putByteArray(MyLoader.ARG_KEY, secretKeyBytes);

            LoaderManager.getInstance(this).restartLoader(LoaderID, args, this);
        });
    }

    // Генерация AES-ключа
    private SecretKey generateKey() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("secure-seed".getBytes()); // для повторяемости
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            return kg.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации ключа", e);
        }
    }

    // Шифрование текста
    private byte[] encryptText(String text, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка шифрования", e);
        }
    }

    // ===== Методы LoaderCallbacks =====

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new MyLoader(this, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String result) {
        Toast.makeText(this, "Расшифровка: " + result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {}
}
