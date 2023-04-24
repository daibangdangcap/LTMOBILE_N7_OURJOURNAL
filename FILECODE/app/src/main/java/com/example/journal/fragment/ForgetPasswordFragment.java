package com.example.journal.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.journal.LoginActivity;
import com.example.journal.MainPageActivity;
import com.example.journal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPasswordFragment extends Fragment {

    ImageButton btback;
    EditText emailbox;
    Button resetpass;
    FirebaseAuth auth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forget_password, container, false);
        btback = view.findViewById(R.id.Forgetpasspage_btback);
        resetpass = view.findViewById(R.id.Forgetpasspage_btsubmit);
        emailbox = view.findViewById(R.id.forgetpasspage_etEmail);
        auth    = FirebaseAuth.getInstance();
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = emailbox.getText().toString().trim();
                if (TextUtils.isEmpty(useremail) && !Patterns.EMAIL_ADDRESS.matcher(useremail).matches())
                {
                    Toast.makeText(getActivity(), "Nhập email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Kiểm tra email của bạn!",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Lỗi gửi email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return view;
    }
}