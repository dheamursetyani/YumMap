package com.example.yummap.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.yummap.R;
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
        btnRegister.setOnClickListener(v -> { // Replaced with lambda
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> { // Replaced with lambda
            strUsername = String.valueOf(inputUser.getText()); // Fixed potential NullPointerException
            strPassword = String.valueOf(inputPassword.getText()); // Fixed potential NullPointerException

            loginViewModel.getDataUser(strUsername, strPassword);

            if (strUsername.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Ups, Form harus diisi semua!",
                        Toast.LENGTH_LONG).show();
            } else {
                loginViewModel.getDataUser(strUsername, strPassword).observe(LoginActivity.this,
                        modelDatabases -> {
                            if (!modelDatabases.isEmpty()) { // Fixed suggestion
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Ups, Username atau Password Anda salah!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}