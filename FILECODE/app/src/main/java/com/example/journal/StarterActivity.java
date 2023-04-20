package com.example.journal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

public class StarterActivity extends AppCompatActivity {
MaterialButton btnLogin, btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        btnLogin = findViewById(R.id.loginTV);
        btnRegister = findViewById(R.id.registerTV);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this).toBundle();
                startActivity(new Intent(StarterActivity.this, Register.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = ActivityOptions.makeSceneTransitionAnimation(StarterActivity.this).toBundle();
                startActivity(new Intent(StarterActivity.this, LoginActivity.class));
            }
        });

    }

}