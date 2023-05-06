package com.example.journal.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.journal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Setting_InfoFragment extends Fragment {


    ImageView btnBack_ChangeUserInform;
    EditText Namechange,DOBchange,Phonechange;
    RadioButton male, female;
    String gender;
    Button submit;
    FirebaseDatabase mDatabse;
    FirebaseUser mUser;
    RadioGroup sex;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting__info, container, false);
        btnBack_ChangeUserInform=view.findViewById(R.id.btnBack_ChangeInfo);
        mDatabse = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        Namechange = view.findViewById(R.id.edUserName_ChangeInfo);
        DOBchange = view.findViewById(R.id.edDOB_ChangeInfo);
        Phonechange = view.findViewById(R.id.edPhone_ChangeInfo);
        submit = view.findViewById(R.id.btnSubmic_ChangeInfo);
        male = view.findViewById(R.id.rbMale_ChangeInfo);
        female = view.findViewById(R.id.rbFemale_ChangeInfo);
        sex = view.findViewById(R.id.rgSex_ChangeInfo);
        btnBack_ChangeUserInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new SettingFragment();
                if(fragment!=null)
                {
                    Navigation.findNavController(view).popBackStack();
                }
            }

        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Namechange.getText().toString().trim();
                String dob = DOBchange.getText().toString().trim();
                String phone = Phonechange.getText().toString().trim();
                String Trai = male.getText().toString().trim();
                String Gai = female.getText().toString().trim();
                if(male.isChecked())
                {
                    gender = Trai;
                }
                if(female.isChecked())
                {
                    gender = Gai;
                }
                // Lấy reference đến node người dùng trong database
                DatabaseReference userRef = mDatabse.getReference("Users").child(mUser.getUid());
                // Cập nhật thông tin người dùng
                userRef.child("fullname").setValue(name);
                userRef.child("dob").setValue(dob);
                userRef.child("gender").setValue(gender);
                userRef.child("phone").setValue(phone);

                Toast.makeText(getActivity(), "Thông tin người dùng đã được cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}