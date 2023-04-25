package com.example.journal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.journal.R;

public class ApplicationPolicyFragment extends Fragment {
    ImageView btnBack_Apllication;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dieu_khoan_ungdung, container, false);
        btnBack_Apllication=view.findViewById(R.id.Policyapp_ivbtback);
        btnBack_Apllication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new Setting_PolicyFragment();
                if(fragment!=null)
                {
                    Navigation.findNavController(view).navigate(R.id.action_applicationPolicyFragment_to_setting_PolicyFragment);
                }
            }
        });
        return view;
    }
}
