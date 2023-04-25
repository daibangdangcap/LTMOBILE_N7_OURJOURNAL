package com.example.journal.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.journal.R;


public class Setting_PasswordFragment extends Fragment {

    ImageView btnBack_ChangePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_setting__password, container, false);
        btnBack_ChangePassword=view.findViewById(R.id.btnBack_ChangePassword);
        btnBack_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new SettingFragment();
                if(fragment!=null)
                {
                    Navigation.findNavController(view).navigate(R.id.action_setting_PasswordFragment_to_settingFragment);

                }
            }
        });
        return view;
    }
}