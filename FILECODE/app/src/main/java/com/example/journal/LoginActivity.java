package com.example.journal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journal.fragment.ForgetPasswordFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        arrowbackregister = findViewById(R.id.arrowback_register);
        etEmailLogin = findViewById(R.id.etUsernameLogin);
        etPasslogin = findViewById(R.id.etPassLogin);
        sendlogin = findViewById(R.id.sendLogin);
        forgotPassword = findViewById(R.id.tvForgetpass);
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
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ForgetPasswordFragment());
            }
        });
    }

    private void loadFragment(Fragment fmNew) {
        FragmentTransaction fmTran = getSupportFragmentManager().beginTransaction();
        fmTran.replace(R.id.TrangDangnhap, fmNew);
        fmTran.addToBackStack(null);
        fmTran.commit();
        sendlogin.setVisibility(View.INVISIBLE);
        forgotPassword.setVisibility(View.INVISIBLE);
    }
}
