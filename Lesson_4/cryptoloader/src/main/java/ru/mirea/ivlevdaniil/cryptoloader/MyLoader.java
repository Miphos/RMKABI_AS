package ru.mirea.ivlevdaniil.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {
    public static final String ARG_ENCRYPTED = "encrypted";
    public static final String ARG_KEY = "key";

    private final byte[] encryptedText;
    private final byte[] keyBytes;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        encryptedText = args.getByteArray(ARG_ENCRYPTED);
        keyBytes = args.getByteArray(ARG_KEY);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad(); // запустить loadInBackground
    }

    @Override
    public String loadInBackground() {
        try {
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(encryptedText);
            return new String(decrypted);
        } catch (Exception e) {
            return "Ошибка расшифровки: " + e.getMessage();
        }
    }
}
