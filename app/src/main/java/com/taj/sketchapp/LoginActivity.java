package com.taj.sketchapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LoginActivity extends AppCompatActivity {

    private ConstraintLayout loginLayout;
    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginLayout = findViewById(R.id.loginLayout);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailAddress.getText().toString();
                String password = editTextPassword.getText().toString();

                if (isValidCredentials(email, password)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    shakeLoginForm();
                }
            }
        });
    }

    private boolean isValidCredentials(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }

    private void shakeLoginForm() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(loginLayout, "translationX", -30, 30, -30, 30, 0);
        animator.setDuration(500);
        animator.start();
    }
}
