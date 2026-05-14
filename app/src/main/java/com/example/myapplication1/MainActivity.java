package com.example.myapplication1;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication1.data.database.AppDatabase;
import com.example.myapplication1.data.entity.User;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatabase();
        initViews();
        initListeners();
        ImageView langSwitch = findViewById(R.id.langSwitch);
        langSwitch.setOnClickListener(v -> {
            String currentLang = getResources().getConfiguration().getLocales().get(0).getLanguage();
            if (currentLang.equals("en")) {
                setLocale("ar");
            } else {
                setLocale("en");
            }
        });
    }
    private void initDatabase() {
        db = AppDatabase.getInstance(this);
    }

    private void initViews() {
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
    }

    private void initListeners() {
        loginButton.setOnClickListener(v -> loginFlow());
    }
    private void loginFlow() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!validateInput(username, password)) return;

        if (isAdmin(username, password)) {
            Toast.makeText(this, "Logged in as Admin", Toast.LENGTH_SHORT).show();
            goToHome(username);
            return;
        }

        User user = db.userDao().getUserByUsername(username);
        if (!validateUser(user, password)) return;

        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        goToHome(username);
    }
    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean isAdmin(String username, String password) {
        return username.equals("admin") && password.equals("1234");
    }
    private boolean validateUser(User user, String password) {
        if (user == null) {
            Toast.makeText(this, "User not found. Please sign up first.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (user.password == null) {
            Toast.makeText(this, " password missing", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!user.password.equals(password)) {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void goToHome(String username) {
        Intent intent = new Intent(MainActivity.this, NavigationDrawer.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        finish();
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);

        getBaseContext().getResources().updateConfiguration(
                config,
                getBaseContext().getResources().getDisplayMetrics()
        );
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(refresh);
        finish();
    }

}

