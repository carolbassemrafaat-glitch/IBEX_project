package com.example.myapplication1;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication1.data.database.AppDatabase;
import com.example.myapplication1.data.entity.User;

public class SignUp extends AppCompatActivity {
    private EditText editUsername, editPassword, editConfirmPassword;
    private Button btnSignUp;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initDatabase();
        initViews();
        initListeners();
    }
    private void initDatabase() {
        db = AppDatabase.getInstance(this);
    }

    private void initViews() {
        editUsername = findViewById(R.id.usernameInput2);
        editPassword = findViewById(R.id.passwordInput2);
        editConfirmPassword = findViewById(R.id.passwordInput3);
        btnSignUp = findViewById(R.id.SignUpbtn);
    }

    private void initListeners() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });
    }
    private void handleSignUp() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        if (!validateInput(username, password, confirmPassword)) return;

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUp.this, "Invalid Password ", Toast.LENGTH_SHORT).show();
            return;
        }

        User existingUser = db.userDao().getUserByUsername(username);
        if (existingUser != null) {
            Toast.makeText(SignUp.this, "Username taken", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(username, password);
        }
    }
    private boolean validateInput(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(SignUp.this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void registerUser(String username, String password) {
        User newUser = new User();
        newUser.username = username;
        newUser.password = password;
        db.userDao().insertUser(newUser);
        Toast.makeText(SignUp.this, "User registered: " + username, Toast.LENGTH_SHORT).show();
        finish();
    }
}
