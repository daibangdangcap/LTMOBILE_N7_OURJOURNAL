package com.example.journal.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.journal.MainPageActivity;
import com.example.journal.R;


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
                Fragment fragment=new Setting_HelpFragment();
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.trangchinh,fragment).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });
        btnPolicy=view.findViewById(R.id.btnPolicy_Setting);
        btnPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new Setting_PolicyFragment();
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.trangchinh,fragment).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });
        btnChangePassword=view.findViewById(R.id.btnChangePassword_Setting);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new Setting_PasswordFragment();
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.trangchinh,fragment).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });
        btnChangeUserInform=view.findViewById(R.id.btnChangeInfo_Setting);
        btnChangeUserInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new Setting_InfoFragment();
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.trangchinh,fragment).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });
        btnBack=view.findViewById(R.id.btnBack_Setting);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainPageActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        btnAboutUs=view.findViewById(R.id.btnAboutUs_Setting);
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new AboutUsFragment();
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.trangchinh,fragment).commit();
                fragmentTransaction.addToBackStack(null);
            }
        });
        return view;
    }
}