package com.example.journal.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.journal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;


public class Setting_PasswordFragment extends Fragment {

    ImageView btnBack_ChangePassword;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText currentpass , newpass;
    Button submit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting__password, container, false);
        btnBack_ChangePassword = view.findViewById(R.id.btnBack_ChangePassword);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // Lấy reference đến các view trong fragment
        currentpass = view.findViewById(R.id.edPresentPassword_ChangePassword);
        newpass = view.findViewById(R.id.edNewwPassword_ChangePassword);
        submit = view.findViewById(R.id.btnSubmic_ChangePassword);
        btnBack_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SettingFragment();
                if (fragment != null) {
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = currentpass.getText().toString().trim();
                String newPassword = newpass.getText().toString().trim();

                // Kiểm tra xem mật khẩu hiện tại có chính xác hay không
                AuthCredential credential = EmailAuthProvider.getCredential(mUser.getEmail(), currentPassword);
                mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Đổi mật khẩu
                            mUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Mật khẩu đã được đổi thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Có lỗi xảy ra khi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Mật khẩu hiện tại không chính xác", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }

}