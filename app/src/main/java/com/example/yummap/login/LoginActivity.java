package com.example.yummap.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.yummap.R;
import com.example.yummap.database.UserModel;
import com.example.yummap.main.MainActivity;
import com.example.yummap.register.RegisterActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialButton btnLogin, btnRegister;
    TextInputEditText inputUser, inputPassword;
    LoginViewModel loginViewModel;
    String strUsername, strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setInitLayout();
        setInputData();
    }

    private void setInitLayout() {
        inputUser = findViewById(R.id.inputUser);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        loginViewModel = new ViewModelProvider(LoginActivity.this).get(LoginViewModel.class);
    }

    private void setInputData() {
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            strUsername = String.valueOf(inputUser.getText());
            strPassword = String.valueOf(inputPassword.getText());

            if (strUsername.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Ups, Form harus diisi semua!",
                        Toast.LENGTH_LONG).show();
            } else {
                loginViewModel.getDataUser(strUsername, strPassword).observe(LoginActivity.this,
                        modelDatabases -> {
                            if (!modelDatabases.isEmpty()) {
                                // Login successful, get username from UserModel
                                UserModel user = modelDatabases.get(0); // First user in the list
                                String username = user.getUsername();

                                // Navigate to MainActivity and pass username
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("USERNAME", username);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Ups, Username atau Password Anda salah!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}