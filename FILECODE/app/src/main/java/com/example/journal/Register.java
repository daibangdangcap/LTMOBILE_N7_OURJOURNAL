package com.example.journal;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    FirebaseFirestore db;
    ImageButton arrowback_register;
    Button btnRegister;
    RadioGroup rgSex;
    RadioButton rbMale, rbFemale;
    String gender ="";
    EditText etDOBReg, etPasswordReg, etReenterReg, etEmailReg, etPhoneReg, etFullname;
    //FIREBASE
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        arrowback_register = findViewById(R.id.arrowback_register);
        etFullname = findViewById(R.id.etName_Register);;
        etEmailReg = findViewById(R.id.etEmail_Registerr);
        etPasswordReg = findViewById(R.id.etPassword_register);
        etReenterReg = findViewById(R.id.etReenterPassword_register);
        etDOBReg = findViewById(R.id.etDOB_register);
        etPhoneReg = findViewById(R.id.etPhonenumber_register);
        btnRegister = findViewById(R.id.btnRegisterr);
        rgSex = findViewById(R.id.rgSex);
        rbMale  = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        //firebase
        auth = FirebaseAuth.getInstance();


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
    boolean checkInfo(String fullname, String dob, String password, String reenter, String email, String phone) {
        if (fullname.isEmpty() || password.isEmpty() || reenter.isEmpty() || email.isEmpty() || phone.isEmpty()
        || dob.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    boolean checkEmail(String username)
    {

        if (username.length()<=5)
        {
            etEmailReg.setError("Tên đăng nhập phải ít nhất 6 ký tự!");
            etEmailReg.requestFocus();
            return false;
        }
        return true;
    }
     boolean checkPassword(String password, String confirmPassword)
    {
        if (password.length()<= 5)
        {
            etPasswordReg.setError("Mật khẩu phải lớn hơn 5 ký tự!");
            etPasswordReg.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword))
        {
            etReenterReg.setError("Xác nhận mật khẩu không khớp!");
            etReenterReg.requestFocus();
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
            etPhoneReg.requestFocus();
            return false;
        }
    }
    void sukienRegister()
    {
        //sex =1 la male, sex =0 la female

        //firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        String fullname = etFullname.getText().toString().trim();
        String email = etEmailReg.getText().toString().trim();
        String password = etPasswordReg.getText().toString().trim();
        String reenter = etReenterReg.getText().toString().trim();
        String dob = etDOBReg.getText().toString().trim();
        String phone = etPhoneReg.getText().toString().trim();
        String imageAvatar="";
        boolean isValid = checkInfo(fullname, dob,password, reenter, email, phone) && checkEmail(email) && checkPassword(password, reenter) && checkPhone(phone);
        if (rbFemale.isChecked())
        {
            gender="Nữ";
        }
        if (rbMale.isChecked())
        {
            gender ="Nam";
        }
        if (isValid)
        {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userID  = user.getUid();
                        HelperClass information = new HelperClass(userID,fullname, password, email,phone,gender,imageAvatar);
                        FirebaseDatabase.getInstance().getReference("Users").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register.this, LoginActivity.class));
                                    }
                                });
                        db = FirebaseFirestore.getInstance();
                        Map<String,Object> datafriendRequest=new HashMap<String, Object>();
                        db.collection("FriendRequest").document(userID).set(datafriendRequest);
                    }

                    else {
                        Toast.makeText(Register.this, "Đăng ký thất bại!" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

       // reference.child(username).setValue(helperClass);
       }
    }
}