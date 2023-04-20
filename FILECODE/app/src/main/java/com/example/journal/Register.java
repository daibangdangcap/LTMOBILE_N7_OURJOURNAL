package com.example.journal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    ImageButton arrowback_register;
    Button btnRegister;
    RadioGroup rgSex;
    RadioButton rbMale, rbFemale;
    EditText etUsernameReg, etPasswordReg, etReenterReg, etEmailReg, etPhoneReg;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        arrowback_register = findViewById(R.id.arrowback_register);
        etUsernameReg = findViewById(R.id.etUsername_register);
        etPasswordReg = findViewById(R.id.etPassword_register);
        etReenterReg = findViewById(R.id.etReenterPassword_register);
        etEmailReg = findViewById(R.id.etEmail_register);
        etPhoneReg = findViewById(R.id.etPhonenumber_register);
        btnRegister = findViewById(R.id.btnRegisterr);
        rgSex = findViewById(R.id.rgSex);
        rbMale  = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sukienRegister();
            }
        });
        arrowback_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    boolean checkInfo(String username,String password, String reenter, String email, String phone) {
        if (username.isEmpty() || password.isEmpty() || reenter.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    boolean checkUsername(String username)
    {

        if (username.length()<=5)
        {
            etUsernameReg.setError("Tên đăng nhập phải ít nhất 6 ký tự!");
            return false;
        }
        return true;
    }
     boolean checkPassword(String password, String confirmPassword)
    {
        if (password.length()<= 5)
        {
            etPasswordReg.setError("Mật khẩu phải lớn hơn 5 ký tự!");
            return false;
        }
        if (!password.equals(confirmPassword))
        {
            etReenterReg.setError("Xác nhận mật khẩu không khớp!");
            return false;
        }
        return true;
    }
    boolean checkPhone(String phone)
    {
        if (phone.length()==10)
        {
            return true;
        }
        else
        {
            etPhoneReg.setError("Số điện thoại nhập không đúng!");
            return false;
        }
    }
    void sukienRegister()
    {
        //sex =1 la male, sex =0 la female
        int sex= 1;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");
        String username = etUsernameReg.getText().toString().trim();
        String password = etPasswordReg.getText().toString().trim();
        String reenter = etReenterReg.getText().toString().trim();
        String email = etEmailReg.getText().toString().trim();
        String phone = etPhoneReg.getText().toString().trim();

        boolean isValid = checkInfo(username, password, reenter, email, phone) && checkUsername(username) && checkPassword(password, reenter) && checkPhone(phone);
        int sexSelected =rgSex.getCheckedRadioButtonId();
        if (sexSelected == R.id.rbFemale){
            sex =0;
        }
        if (isValid)
        {
        HelperClass helperClass = new HelperClass(username, password, email, phone, sex);
        reference.child(username).setValue(helperClass);
        Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Register.this, LoginActivity.class));
       }
    }
}