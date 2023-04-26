package com.example.journal.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.journal.MainPageActivity;
import com.example.journal.R;
import com.google.android.material.navigation.NavigationView;


public class SettingFragment extends Fragment {
    ImageView btnBack;
    Button btnSupport;
    Button btnChangeUserInform;
    Button btnChangePassword;
    Button btnPolicy;
    Button btnAboutUs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_setting,container,false);

        btnSupport=view.findViewById(R.id.btnSupport_Setting);
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_setting_HelpFragment);
            }
        });
        btnPolicy=view.findViewById(R.id.btnPolicy_Setting);
        btnPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_setting_PolicyFragment);
            }
        });
        btnChangePassword=view.findViewById(R.id.btnChangePassword_Setting);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_setting_PasswordFragment);
            }
        });
        btnChangeUserInform=view.findViewById(R.id.btnChangeInfo_Setting);
        btnChangeUserInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_setting_InfoFragment);
            }
        });
        btnBack=view.findViewById(R.id.btnBack_Setting);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        btnAboutUs=view.findViewById(R.id.btnAboutUs_Setting);
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_aboutUsFragment);
            }
        });
        return view;
    }
}