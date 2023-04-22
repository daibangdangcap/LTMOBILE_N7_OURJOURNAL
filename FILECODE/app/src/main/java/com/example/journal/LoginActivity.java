package com.example.journal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.util.GAuthToken;

public class LoginActivity extends AppCompatActivity {
    private static final String FILE_USERNAME = "rememberMe";
    //
    ImageButton arrowbackregister;
    EditText etEmailLogin, etPasslogin;
    MaterialButton sendlogin;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        arrowbackregister = findViewById(R.id.arrowback_register);
        etEmailLogin = findViewById(R.id.etUsernameLogin);
        etPasslogin = findViewById(R.id.etPassLogin);
        sendlogin = findViewById(R.id.sendLogin);
        auth = FirebaseAuth.getInstance();

        sendlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String email = etEmailLogin.getText().toString();
              String password = etPasslogin.getText().toString();

              if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                  if (!password.isEmpty()) {
                      auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                          @Override
                          public void onSuccess(AuthResult authResult) {
                              Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
                              finish();
                          }
                      }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                          }
                      });
                  } else {
                      etPasslogin.setError("Vui lòng nhập mật khẩu!");
                      etPasslogin.requestFocus();
                  }
              }
                    else if (email.isEmpty())
                {
                    etEmailLogin.setError("Vui lòng nhập tên đăng nhập!");
                    etEmailLogin.requestFocus();
                }
                    else
                {
                    etEmailLogin.setError("Vui lòng nhập email!");
                }
            }
        });
        arrowbackregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }
}
