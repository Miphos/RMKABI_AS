package ru.mirea.ivlevdaniil.firebaseauth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseAuthDemo";

    private EditText emailInput, passwordInput;
    private Button registerButton, loginButton, logoutButton, verifyButton;
    private TextView statusText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        // Инициализация элементов интерфейса
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
        logoutButton = findViewById(R.id.logoutButton);
        verifyButton = findViewById(R.id.verifyButton);
        statusText = findViewById(R.id.statusText);

        // Инициализация Firebase
        mAuth = FirebaseAuth.getInstance();

        // Обработчики кнопок
        registerButton.setOnClickListener(v -> register());
        loginButton.setOnClickListener(v -> login());
        logoutButton.setOnClickListener(v -> logout());
        verifyButton.setOnClickListener(v -> sendEmailVerification());

        // Обновление интерфейса в зависимости от статуса
        updateUI(mAuth.getCurrentUser());
    }

    private void register() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Регистрация успешна");
                        Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        showError("Ошибка регистрации: " + task.getException().getMessage());
                    }
                });
    }

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Вход выполнен");
                        Toast.makeText(this, "Успешный вход", Toast.LENGTH_SHORT).show();
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        showError("Ошибка входа: " + task.getException().getMessage());
                    }
                });
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(this, "Выход выполнен", Toast.LENGTH_SHORT).show();
        updateUI(null);
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Письмо отправлено", Toast.LENGTH_SHORT).show();
                        } else {
                            showError("Ошибка при отправке письма: " + task.getException().getMessage());
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        boolean isLoggedIn = user != null;

        emailInput.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        passwordInput.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        registerButton.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        loginButton.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);

        logoutButton.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        verifyButton.setVisibility(isLoggedIn && !user.isEmailVerified() ? View.VISIBLE : View.GONE);

        if (isLoggedIn) {
            String text = "Пользователь: " + user.getEmail() +
                    "\nВерифицирован: " + user.isEmailVerified();
            statusText.setText(text);
        } else {
            statusText.setText("Пользователь не авторизован");
        }
    }

    private void showError(String message) {
        Log.e(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
