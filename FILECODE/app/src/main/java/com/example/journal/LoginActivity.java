package com.example.journal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.journal.fragment.ForgetPasswordFragment;
import com.google.android.gms.common.internal.Objects;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String FILE_USERNAME = "rememberMe";
    //
    ImageButton arrowbackregister;
    EditText etUsernamelogin, etPasslogin;
    MaterialButton sendlogin;
    CheckBox saveID;

    TextView forgetpassword;

    String saveUsername;
    String savePassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        arrowbackregister = findViewById(R.id.arrowback_register);
        etUsernamelogin = findViewById(R.id.etUsernameLogin);
        etPasslogin = findViewById(R.id.etPassLogin);
        sendlogin = findViewById(R.id.sendLogin);
        forgetpassword = findViewById(R.id.tvForgetpass);
        //Forgetpassword
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetpass();
                setvisibility_forgetpass();
            }
        });
        //Remember me
        saveID = (CheckBox) findViewById(R.id.tvRememberme);
        SharedPreferences sharedPreferences = getSharedPreferences(FILE_USERNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String saveUsername = sharedPreferences.getString("lgUsername","");
        String savePassword = sharedPreferences.getString("lgPassword","");
        if (sharedPreferences.contains("checked") && sharedPreferences.getBoolean("checked",false)==true)
        {
            saveID.setChecked(true);
        }
        else
        {
            saveID.setChecked(false);
        }
        etUsernamelogin.setText(saveUsername);
        etPasslogin.setText(savePassword);


        sendlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveID.isChecked())
                {
                    editor.putBoolean("checked",true);
                    editor.apply();
                    StoreDataUsingSharedPref(saveUsername, savePassword);

                    if (!checkUsername() | !checkPassword())
                    {
                    }
                    else
                    {
                        checkUser();
                    }
                }
                else
                {
                    if (!checkUsername() | !checkPassword())
                    {
                    }
                    else
                    {
                        checkUser();
                     }

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

    private void StoreDataUsingSharedPref(String saveUsername, String savePassword) {
        SharedPreferences.Editor editor = getSharedPreferences(FILE_USERNAME, MODE_PRIVATE).edit();
        editor.putString("lgUsername", saveUsername);
        editor.putString("lgPassword", savePassword);

    }

    boolean checkUsername() {
        String val = etUsernamelogin.getText().toString();
        if (val.isEmpty()) {
            etUsernamelogin.setError("Vui lòng nhập tên đăng nhập!");
            etUsernamelogin.requestFocus();
            return false;
        }
            return true;
    }
    boolean checkPassword()
    {
        String val = etPasslogin.getText().toString();
        if (val.isEmpty()) {
            etPasslogin.setError("Vui lòng nhập mật khẩu!");
            etPasslogin.requestFocus();
            return false;
        }
            return true;
    }
    public void checkUser()
    {
        String username = etUsernamelogin.getText().toString().trim();
        String password = etPasslogin.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String passwordDB = snapshot.child(username).child("password").getValue(String.class);

                    if (passwordDB.equals(password))
                    {
                        startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
                    }
                    else {
                        etPasslogin.setError("Sai mật khẩu");
                        etPasslogin.requestFocus();
                    }
                }
                else  {
                    etUsernamelogin.setError("Người dùng không tồn tại");
                    etUsernamelogin.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void forgetpass ()
    {
        Fragment fragment = new ForgetPasswordFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.TrangDangnhap,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    void setvisibility_forgetpass()
    {
        arrowbackregister.setVisibility(View.INVISIBLE);
        etPasslogin.setVisibility(View.INVISIBLE);
        etUsernamelogin.setVisibility(View.INVISIBLE);
        sendlogin.setVisibility((View.INVISIBLE));
        saveID.setVisibility(View.INVISIBLE);
    }
}
